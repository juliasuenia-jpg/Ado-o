package sistema.adocao.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sistema.adocao.dto.AnimalFiltro;
import sistema.adocao.dto.AnimalForm;
import sistema.adocao.model.Animal;
import sistema.adocao.model.StatusAnimal;
import sistema.adocao.model.Usuario;
import sistema.adocao.repository.AnimalRepository;
import sistema.adocao.repository.SolicitacaoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animais;
    private final SolicitacaoRepository solicitacoes;
    private final ArmazenamentoService armazenamento;

    public AnimalService(AnimalRepository animais, SolicitacaoRepository solicitacoes,
                         ArmazenamentoService armazenamento) {
        this.animais = animais;
        this.solicitacoes = solicitacoes;
        this.armazenamento = armazenamento;
    }

    /** Busca avançada: só animais disponíveis, combinando todos os filtros informados. */
    public List<Animal> buscar(AnimalFiltro f) {
        Specification<Animal> spec = (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            p.add(cb.equal(root.get("status"), StatusAnimal.DISPONIVEL));
            if (f.getEspecie() != null) p.add(cb.equal(root.get("especie"), f.getEspecie()));
            if (f.getPorte() != null) p.add(cb.equal(root.get("porte"), f.getPorte()));
            if (f.getSexo() != null) p.add(cb.equal(root.get("sexo"), f.getSexo()));
            if (f.getConvivePets() != null) p.add(cb.equal(root.get("convivePets"), f.getConvivePets()));
            if (f.getCidade() != null && !f.getCidade().isBlank()) {
                String termo = "%" + f.getCidade().trim().toLowerCase() + "%";
                p.add(cb.like(cb.lower(root.get("cidade")), termo));
            }
            if (f.getFaixaEtaria() != null) {
                switch (f.getFaixaEtaria()) {
                    case FILHOTE -> p.add(cb.lessThan(root.<Integer>get("idadeMeses"), 12));
                    case ADULTO -> p.add(cb.between(root.<Integer>get("idadeMeses"), 12, 95));
                    case IDOSO -> p.add(cb.greaterThanOrEqualTo(root.<Integer>get("idadeMeses"), 96));
                }
            }
            return cb.and(p.toArray(new Predicate[0]));
        };
        return animais.findAll(spec, Sort.by(Sort.Direction.DESC, "criadoEm"));
    }

    public Animal salvar(Long id, AnimalForm form, Usuario autor) throws IOException {
        Animal a;
        if (id == null) {
            a = new Animal();
            a.setCadastradoPor(autor);
        } else {
            a = animais.findById(id).orElseThrow();
        }
        a.setNome(form.getNome().trim());
        a.setEspecie(form.getEspecie());
        a.setRaca(vazioParaNulo(form.getRaca()));
        a.setPorte(form.getPorte());
        a.setSexo(form.getSexo());
        a.setIdadeMeses(form.totalMeses());
        a.setCidade(form.getCidade().trim());
        a.setConvivePets(form.isConvivePets());
        a.setDescricao(vazioParaNulo(form.getDescricao()));
        a.setVacinado(form.isVacinado());
        a.setCastrado(form.isCastrado());
        a.setVermifugado(form.isVermifugado());
        a.setObservacoesSaude(vazioParaNulo(form.getObservacoesSaude()));
        if (id != null && form.getStatus() != null) a.setStatus(form.getStatus());

        if (form.getFoto() != null && !form.getFoto().isEmpty()) {
            String novaFoto = armazenamento.salvar(form.getFoto());
            armazenamento.remover(a.getFoto());
            a.setFoto(novaFoto);
        }
        return animais.save(a);
    }

    /** Animais com solicitações não podem ser apagados: o histórico precisa ser preservado. */
    public void excluir(Long id) {
        Animal a = animais.findById(id).orElseThrow();
        if (solicitacoes.existsByAnimal(a)) {
            throw new IllegalStateException(
                    "Este animal tem solicitações de adoção registradas e não pode ser removido. Marque-o como adotado.");
        }
        armazenamento.remover(a.getFoto());
        animais.delete(a);
    }

    private static String vazioParaNulo(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
