package sistema.adocao.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sistema.adocao.model.StatusAnimal;
import sistema.adocao.repository.AnimalRepository;

@Controller
public class HomeController {

    private final AnimalRepository animais;

    public HomeController(AnimalRepository animais) {
        this.animais = animais;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentes", animais.findTop6ByStatusOrderByCriadoEmDesc(StatusAnimal.DISPONIVEL));
        model.addAttribute("totalDisponiveis", animais.countByStatus(StatusAnimal.DISPONIVEL));
        model.addAttribute("totalAdotados", animais.countByStatus(StatusAnimal.ADOTADO));
        return "index";
    }

    /** Depois do login, administradores vão direto para o painel. */
    @GetMapping("/entrar-redirect")
    public String aposLogin(Authentication auth) {
        boolean admin = auth != null && auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMINISTRADOR"));
        return "redirect:" + (admin ? "/admin" : "/");
    }
}
