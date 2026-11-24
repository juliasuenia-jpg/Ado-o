package sistema.adocao.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sistema.adocao.dto.CadastroForm;
import sistema.adocao.model.Perfil;
import sistema.adocao.model.Usuario;
import sistema.adocao.repository.UsuarioRepository;

@Controller
public class AuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("form", new CadastroForm());
        return "auth/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute("form") CadastroForm form, Model model, RedirectAttributes ra) {
        String erro = validar(form);
        if (erro != null) {
            model.addAttribute("erro", erro);
            return "auth/cadastro";
        }
        Usuario u = new Usuario();
        u.setNome(form.getNome().trim());
        u.setEmail(form.getEmail().trim().toLowerCase());
        u.setSenha(encoder.encode(form.getSenha()));
        u.setTelefone(form.getTelefone() == null || form.getTelefone().isBlank() ? null : form.getTelefone().trim());
        u.setCidade(form.getCidade() == null || form.getCidade().isBlank() ? null : form.getCidade().trim());
        u.setPerfil(Perfil.ADOTANTE);   // contas criadas pelo site são sempre de adotante
        usuarios.save(u);

        ra.addFlashAttribute("sucesso", "Conta criada! Entre com seu e-mail e senha.");
        return "redirect:/login";
    }

    private String validar(CadastroForm f) {
        if (vazio(f.getNome())) return "Informe seu nome.";
        if (vazio(f.getEmail()) || !f.getEmail().contains("@")) return "Informe um e-mail válido.";
        if (usuarios.existsByEmail(f.getEmail().trim().toLowerCase())) return "Já existe uma conta com este e-mail.";
        if (vazio(f.getSenha()) || f.getSenha().length() < 6) return "A senha precisa ter pelo menos 6 caracteres.";
        if (!f.getSenha().equals(f.getConfirmarSenha())) return "A confirmação não é igual à senha.";
        return null;
    }

    private boolean vazio(String s) { return s == null || s.isBlank(); }
}
