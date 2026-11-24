package sistema.adocao.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import sistema.adocao.model.*;
import sistema.adocao.repository.UsuarioRepository;

import java.security.Principal;

/** Disponibiliza o usuário logado e as listas de opções para todas as páginas. */
@ControllerAdvice
public class GlobalModelAdvice {

    private final UsuarioRepository usuarios;

    public GlobalModelAdvice(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado(Principal principal) {
        if (principal == null) return null;
        return usuarios.findByEmail(principal.getName()).orElse(null);
    }

    @ModelAttribute("especies")
    public Especie[] especies() { return Especie.values(); }

    @ModelAttribute("portes")
    public Porte[] portes() { return Porte.values(); }

    @ModelAttribute("sexos")
    public Sexo[] sexos() { return Sexo.values(); }

    @ModelAttribute("faixas")
    public FaixaEtaria[] faixas() { return FaixaEtaria.values(); }

    @ModelAttribute("statusSolicitacoes")
    public StatusSolicitacao[] statusSolicitacoes() { return StatusSolicitacao.values(); }

    @ModelAttribute("statusAnimais")
    public StatusAnimal[] statusAnimais() { return StatusAnimal.values(); }
}
