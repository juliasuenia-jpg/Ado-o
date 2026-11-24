package sistema.adocao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sistema.adocao.dto.AnimalForm;
import sistema.adocao.model.*;
import sistema.adocao.repository.AnimalRepository;
import sistema.adocao.repository.SolicitacaoRepository;
import sistema.adocao.repository.UsuarioRepository;
import sistema.adocao.service.AdocaoService;
import sistema.adocao.service.AnimalService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/** Painel da ONG / gestor: dashboard de solicitações e gestão (CRUD) dos animais. */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AnimalRepository animais;
    private final AnimalService animalService;
    private final SolicitacaoRepository solicitacoes;
    private final AdocaoService adocaoService;
    private final UsuarioRepository usuarios;

    public AdminController(AnimalRepository animais, AnimalService animalService,
                           SolicitacaoRepository solicitacoes, AdocaoService adocaoService,
                           UsuarioRepository usuarios) {
        this.animais = animais;
        this.animalService = animalService;
        this.solicitacoes = solicitacoes;
        this.adocaoService = adocaoService;
        this.usuarios = usuarios;
    }

    // ---------- Dashboard de solicitações ----------

    @GetMapping
    public String dashboard(@RequestParam(required = false) StatusSolicitacao status, Model model) {
        List<Solicitacao> lista = status == null
                ? solicitacoes.findAllByOrderByCriadoEmDesc()
                : solicitacoes.findByStatusOrderByCriadoEmDesc(status);
        model.addAttribute("lista", lista);
        model.addAttribute("filtroStatus", status);
        model.addAttribute("qtdPendentes", solicitacoes.countByStatus(StatusSolicitacao.PENDENTE));
        model.addAttribute("qtdEmAnalise", solicitacoes.countByStatus(StatusSolicitacao.EM_ANALISE));
        model.addAttribute("qtdAprovadas", solicitacoes.countByStatus(StatusSolicitacao.APROVADO));
        model.addAttribute("qtdRecusadas", solicitacoes.countByStatus(StatusSolicitacao.RECUSADO));
        return "admin/dashboard";
    }

    @GetMapping("/solicitacoes/{id}")
    public String solicitacao(@PathVariable Long id, Model model) {
        model.addAttribute("s", solicitacoes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/solicitacao";
    }

    @PostMapping("/solicitacoes/{id}/status")
    public String atualizarStatus(@PathVariable Long id, @RequestParam StatusSolicitacao status,
                                  @RequestParam(required = false) String parecer, RedirectAttributes ra) {
        try {
            adocaoService.atualizarStatus(id, status, parecer);
            ra.addFlashAttribute("sucesso", "Solicitação atualizada para “" + status.getRotulo() + "”.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/solicitacoes/" + id;
    }

    // ---------- CRUD de animais ----------

    @GetMapping("/animais")
    public String listarAnimais(Model model) {
        model.addAttribute("animais", animais.findAllByOrderByCriadoEmDesc());
        return "admin/animais";
    }

    @GetMapping("/animais/novo")
    public String novo(Model model) {
        model.addAttribute("form", new AnimalForm());
        model.addAttribute("animalId", null);
        return "admin/animal-form";
    }

    @GetMapping("/animais/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Animal a = animais.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("form", AnimalForm.de(a));
        model.addAttribute("animalId", id);
        model.addAttribute("fotoAtual", a.getFoto());
        return "admin/animal-form";
    }

    @PostMapping("/animais")
    public String criar(@ModelAttribute("form") AnimalForm form, Principal principal, Model model, RedirectAttributes ra) {
        return gravar(null, form, principal, model, ra);
    }

    @PostMapping("/animais/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute("form") AnimalForm form,
                            Principal principal, Model model, RedirectAttributes ra) {
        model.addAttribute("fotoAtual", animais.findById(id).map(Animal::getFoto).orElse(null));
        return gravar(id, form, principal, model, ra);
    }

    @PostMapping("/animais/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            animalService.excluir(id);
            ra.addFlashAttribute("sucesso", "Animal removido.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/animais";
    }

    private String gravar(Long id, AnimalForm form, Principal principal, Model model, RedirectAttributes ra) {
        String erro = validar(form);
        if (erro == null) {
            try {
                Usuario autor = usuarios.findByEmail(principal.getName()).orElseThrow();
                Animal salvo = animalService.salvar(id, form, autor);
                ra.addFlashAttribute("sucesso", "Cadastro de " + salvo.getNome() + " salvo.");
                return "redirect:/admin/animais";
            } catch (IllegalArgumentException e) {
                erro = e.getMessage();
            } catch (IOException e) {
                erro = "Não foi possível salvar a foto. Tente novamente.";
            }
        }
        model.addAttribute("erro", erro);
        model.addAttribute("animalId", id);
        return "admin/animal-form";
    }

    private String validar(AnimalForm f) {
        if (f.getNome() == null || f.getNome().isBlank()) return "Informe o nome do animal.";
        if (f.getCidade() == null || f.getCidade().isBlank()) return "Informe a cidade onde o animal está.";
        if (f.getIdadeAnos() < 0 || f.getIdadeMeses() < 0 || f.getIdadeMeses() > 11) return "Idade inválida (meses de 0 a 11).";
        if (f.totalMeses() > 360) return "Idade inválida.";
        return null;
    }
}
