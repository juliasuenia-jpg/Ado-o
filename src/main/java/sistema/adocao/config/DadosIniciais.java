package sistema.adocao.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sistema.adocao.model.*;
import sistema.adocao.repository.AnimalRepository;
import sistema.adocao.repository.UsuarioRepository;

/** Cria o administrador padrão e alguns animais de exemplo na primeira execução. */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarios;
    private final AnimalRepository animais;
    private final PasswordEncoder encoder;
    private final boolean ativo;

    public DadosIniciais(UsuarioRepository usuarios, AnimalRepository animais, PasswordEncoder encoder,
                         @Value("${app.seed:true}") boolean ativo) {
        this.usuarios = usuarios;
        this.animais = animais;
        this.encoder = encoder;
        this.ativo = ativo;
    }

    @Override
    public void run(String... args) {
        if (!ativo) return;

        Usuario admin = usuarios.findByEmail("admin@adocao.com").orElseGet(() -> {
            Usuario u = new Usuario();
            u.setNome("Administrador");
            u.setEmail("admin@adocao.com");
            u.setSenha(encoder.encode("admin123"));
            u.setPerfil(Perfil.ADMINISTRADOR);
            u.setCidade("Natal");
            return usuarios.save(u);
        });

        if (animais.count() > 0) return;

        animais.save(animal(admin, "Thor", Especie.CACHORRO, "Vira-lata", Porte.GRANDE, Sexo.MACHO, 30, "Natal", true,
                "Brincalhão e muito carinhoso. Adora correr e se dá bem com crianças.", true, false, true,
                "Precisa ser castrado antes de ir para o novo lar."));
        animais.save(animal(admin, "Mel", Especie.CACHORRO, "Poodle", Porte.PEQUENO, Sexo.FEMEA, 8, "Mossoró", true,
                "Filhote curiosa e sociável, já aprendeu a fazer as necessidades no lugar certo.", true, false, true,
                "Primeiras doses de vacina em dia."));
        animais.save(animal(admin, "Mingau", Especie.GATO, "SRD", Porte.PEQUENO, Sexo.MACHO, 4, "Natal", true,
                "Gatinho dorminhoco que ronrona no colo de qualquer pessoa.", true, false, true, null));
        animais.save(animal(admin, "Luna", Especie.GATO, "Siamês", Porte.PEQUENO, Sexo.FEMEA, 30, "Caicó", false,
                "Tranquila e independente. Prefere ser a única gata da casa.", true, true, true, null));
        animais.save(animal(admin, "Bob", Especie.CACHORRO, "SRD", Porte.MEDIO, Sexo.MACHO, 108, "Mossoró", true,
                "Idoso de focinho branco, calmo e companheiro. Ideal para uma rotina tranquila.", true, true, true,
                "Faz acompanhamento veterinário de rotina para as articulações."));
        animais.save(animal(admin, "Pipoca", Especie.CACHORRO, "Shih-tzu", Porte.PEQUENO, Sexo.FEMEA, 14, "Natal", false,
                "Esperta e cheia de energia. Melhor adaptação em casa com tutor presente.", true, false, true, null));
    }

    private Animal animal(Usuario autor, String nome, Especie especie, String raca, Porte porte, Sexo sexo,
                          int meses, String cidade, boolean convive, String descricao,
                          boolean vacinado, boolean castrado, boolean vermifugado, String obs) {
        Animal a = new Animal();
        a.setCadastradoPor(autor);
        a.setNome(nome);
        a.setEspecie(especie);
        a.setRaca(raca);
        a.setPorte(porte);
        a.setSexo(sexo);
        a.setIdadeMeses(meses);
        a.setCidade(cidade);
        a.setConvivePets(convive);
        a.setDescricao(descricao);
        a.setVacinado(vacinado);
        a.setCastrado(castrado);
        a.setVermifugado(vermifugado);
        a.setObservacoesSaude(obs);
        return a;
    }
}
