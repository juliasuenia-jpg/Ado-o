package sistema.adocao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import sistema.adocao.dto.AnimalFiltro;
import sistema.adocao.model.Animal;
import sistema.adocao.repository.AnimalRepository;
import sistema.adocao.service.AnimalService;

/** Área pública: qualquer visitante pode buscar e ver os animais. */
@Controller
public class AnimalController {

    private final AnimalService service;
    private final AnimalRepository animais;

    public AnimalController(AnimalService service, AnimalRepository animais) {
        this.service = service;
        this.animais = animais;
    }

    @GetMapping("/animais")
    public String listar(@ModelAttribute("filtro") AnimalFiltro filtro, Model model) {
        model.addAttribute("resultado", service.buscar(filtro));
        return "animais/lista";
    }

    @GetMapping("/animais/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Animal animal = animais.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("animal", animal);
        return "animais/detalhe";
    }
}
