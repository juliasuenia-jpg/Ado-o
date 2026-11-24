package sistema.adocao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.adocao.dto.SolicitacaoForm;
import sistema.adocao.model.*;
import sistema.adocao.repository.AnimalRepository;
import sistema.adocao.repository.SolicitacaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdocaoService {

    private static final List<StatusSolicitacao> ATIVAS =
            List.of(StatusSolicitacao.PENDENTE, StatusSolicitacao.EM_ANALISE, StatusSolicitacao.APROVADO);

    private final SolicitacaoRepository solicitacoes;
    private final AnimalRepository animais;

    public AdocaoService(SolicitacaoRepository solicitacoes, AnimalRepository animais) {
        this.solicitacoes = solicitacoes;
        this.animais = animais;
    }

    public boolean jaSolicitou(Animal animal, Usuario adotante) {
        return solicitacoes.existsByAnimalAndAdotanteAndStatusIn(animal, adotante, ATIVAS);
    }

    @Transactional
    public Solicitacao solicitar(Animal animal, Usuario adotante, SolicitacaoForm f) {
        if (animal.getStatus() == StatusAnimal.ADOTADO) {
            throw new IllegalStateException("Este animal já foi adotado.");
        }
        if (jaSolicitou(animal, adotante)) {
            throw new IllegalStateException("Você já tem uma solicitação em andamento para este animal.");
        }
        Solicitacao s = new Solicitacao();
        s.setAnimal(animal);
        s.setAdotante(adotante);
        s.setTipoMoradia(f.getTipoMoradia());
        s.setPossuiQuintal(f.isPossuiQuintal());
        s.setMoradiaPermiteAnimais(f.isMoradiaPermiteAnimais());
        s.setOutrosAnimais(f.isOutrosAnimais());
        s.setDescricaoOutrosAnimais(f.isOutrosAnimais() && f.getDescricaoOutrosAnimais() != null
                && !f.getDescricaoOutrosAnimais().isBlank() ? f.getDescricaoOutrosAnimais().trim() : null);
        s.setExperiencia(f.isExperiencia());
        s.setHorasSozinho(f.getHorasSozinho());
        s.setTodosConcordam(f.isTodosConcordam());
        s.setMotivo(f.getMotivo().trim());
        s.setAceitaTermo(f.isAceitaTermo());
        s.setDataVisita(f.getDataVisita());
        s.setCompatibilidade(calcularCompatibilidade(animal, f));
        return solicitacoes.save(s);
    }

    /**
     * Triagem inicial automática (0 a 100). Serve só de apoio: a decisão final é sempre da ONG.
     * Critérios: concordância da família (25), experiência prévia (20), tempo sozinho (até 20),
     * espaço adequado ao porte (20) e convivência com outros animais (15).
     */
    public static int calcularCompatibilidade(Animal animal, SolicitacaoForm f) {
        int pontos = 0;
        if (f.isTodosConcordam()) pontos += 25;
        if (f.isExperiencia()) pontos += 20;

        int horas = f.getHorasSozinho() == null ? 24 : f.getHorasSozinho();
        if (horas <= 8) pontos += 20;
        else if (horas <= 10) pontos += 10;

        if (animal.getPorte() != Porte.GRANDE) {
            pontos += 20;
        } else if (f.isPossuiQuintal() || "Sítio ou chácara".equals(f.getTipoMoradia())) {
            pontos += 20;
        }

        if (!f.isOutrosAnimais() || animal.isConvivePets()) pontos += 15;
        return pontos;
    }

    @Transactional
    public void atualizarStatus(Long id, StatusSolicitacao novo, String parecer) {
        Solicitacao s = solicitacoes.findById(id).orElseThrow();
        StatusSolicitacao anterior = s.getStatus();
        Animal animal = s.getAnimal();

        if (novo == StatusSolicitacao.APROVADO && anterior != StatusSolicitacao.APROVADO
                && animal.getStatus() == StatusAnimal.ADOTADO) {
            throw new IllegalStateException("Este animal já foi adotado por outro candidato.");
        }

        s.setStatus(novo);
        s.setParecerOng(parecer == null || parecer.isBlank() ? null : parecer.trim());
        s.setAtualizadoEm(LocalDateTime.now());
        solicitacoes.save(s);

        if (novo == StatusSolicitacao.APROVADO && anterior != StatusSolicitacao.APROVADO) {
            animal.setStatus(StatusAnimal.ADOTADO);
            animais.save(animal);
            for (Solicitacao outra : solicitacoes.findByAnimalAndIdNot(animal, s.getId())) {
                if (outra.getStatus() == StatusSolicitacao.PENDENTE || outra.getStatus() == StatusSolicitacao.EM_ANALISE) {
                    outra.setStatus(StatusSolicitacao.RECUSADO);
                    outra.setParecerOng("Este animal foi adotado por outro candidato.");
                    outra.setAtualizadoEm(LocalDateTime.now());
                    solicitacoes.save(outra);
                }
            }
        } else if (anterior == StatusSolicitacao.APROVADO && novo != StatusSolicitacao.APROVADO) {
            animal.setStatus(StatusAnimal.DISPONIVEL);
            animais.save(animal);
        }
    }
}
