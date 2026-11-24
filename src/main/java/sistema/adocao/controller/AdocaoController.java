package sistema.adocao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sistema.adocao.dto.SolicitacaoForm;
import sistema.adocao.model.Animal;
import sistema.adocao.model.StatusAnimal;
import sistema.adocao.model.Usuario;
import sistema.adocao.repository.AnimalRepository;
import sistema.adocao.repository.SolicitacaoRepository;
import sistema.adocao.repository.UsuarioRepository;
import sistema.adocao.service.AdocaoService;

import java.security.Principal;
import java.time.LocalDate;

/** Fluxo do adotante: triagem (questionário), agendamento da visita e acompanhamento. */
@Controller
public class AdocaoController {

    private final AnimalRepository animais;
    private final UsuarioRepository usuarios;
    private final SolicitacaoRepository solicitacoes;
    private final AdocaoService service;

    public AdocaoController(AnimalRepository animais, UsuarioRepository usuarios,
                            SolicitacaoRepository solicitacoes, AdocaoService service) {
        this.animais = animais;
        this.usuarios = usuarios;
        this.solicitacoes = solicitacoes;
        this.service = service;
    }

    @GetMapping("/animais/{id}/adotar")
    public String formulario(@PathVariable Long id, Principal principal, Model model, RedirectAttributes ra) {
        Animal animal = buscarAnimal(id);
        if (animal.getStatus() == StatusAnimal.ADOTADO) {
            ra.addFlashAttribute("erro", "Este animal já foi adotado.");
            return "redirect:/animais/" + id;
        }
        if (service.jaSolicitou(animal, usuarioAtual(principal))) {
            ra.addFlashAttribute("erro", "Você já tem uma solicitação para este animal. Acompanhe em “Minhas solicitações”.");
            return "redirect:/minhas-solicitacoes";
        }
        preparar(model, animal, new SolicitacaoForm());
        return "adocao/formulario";
    }

    @PostMapping("/animais/{id}/adotar")
    public String enviar(@PathVariable Long id, @ModelAttribute("form") SolicitacaoForm form,
                         Principal principal, Model model, RedirectAttributes ra) {
        Animal animal = buscarAnimal(id);
        String erro = validar(form);
        if (erro == null) {
            try {
                service.solicitar(animal, usuarioAtual(principal), form);
                ra.addFlashAttribute("sucesso",
                        "Solicitação enviada! A ONG vai analisar suas respostas e confirmar a visita.");
                return "redirect:/minhas-solicitacoes";
            } catch (IllegalStateException e) {
                erro = e.getMessage();
            }
        }
        model.addAttribute("erro", erro);
        preparar(model, animal, form);
        return "adocao/formulario";
    }

    @GetMapping("/minhas-solicitacoes")
    public String minhas(Principal principal, Model model) {
        model.addAttribute("solicitacoes",
                solicitacoes.findByAdotanteOrderByCriadoEmDesc(usuarioAtual(principal)));
        return "adocao/minhas";
    }

    // ---------- auxiliares ----------

    private String validar(SolicitacaoForm f) {
        if (f.getTipoMoradia() == null || f.getTipoMoradia().isBlank()) return "Informe o tipo de moradia.";
        if (f.getHorasSozinho() == null || f.getHorasSozinho() < 0 || f.getHorasSozinho() > 24)
            return "Informe quantas horas por dia o animal ficaria sozinho (de 0 a 24).";
        if (f.getMotivo() == null || f.getMotivo().isBlank()) return "Conte por que você quer adotar.";
        if (f.getMotivo().length() > 1000) return "O motivo pode ter no máximo 1000 caracteres.";
        if (!f.isMoradiaPermiteAnimais()) return "É preciso que o local onde você mora permita animais para seguir com a adoção.";
        if (!f.isAceitaTermo()) return "Aceite o termo de responsabilidade para continuar.";
        if (f.getDataVisita() == null) return "Escolha uma data para a visita.";
        if (!f.getDataVisita().isAfter(LocalDate.now())) return "A visita precisa ser agendada a partir de amanhã.";
        return null;
    }

    private void preparar(Model model, Animal animal, SolicitacaoForm form) {
        model.addAttribute("animal", animal);
        model.addAttribute("form", form);
        model.addAttribute("amanha", LocalDate.now().plusDays(1));
    }

    private Animal buscarAnimal(Long id) {
        return animais.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Usuario usuarioAtual(Principal p) {
        return usuarios.findByEmail(p.getName()).orElseThrow();
    }
}
