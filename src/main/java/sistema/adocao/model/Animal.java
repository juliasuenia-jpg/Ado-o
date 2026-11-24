package sistema.adocao.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Especie especie;

    @Column(length = 80)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Porte porte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sexo sexo;

    /** Idade total em meses (facilita o filtro por faixa etária). */
    @Column(nullable = false)
    private int idadeMeses;

    @Column(nullable = false, length = 100)
    private String cidade;

    /** Convive bem com outros animais? */
    @Column(nullable = false)
    private boolean convivePets;

    @Column(length = 1000)
    private String descricao;

    /** Nome do arquivo da foto (salvo na pasta de uploads). */
    @Column(length = 100)
    private String foto;

    // ---- Ficha médica ----
    @Column(nullable = false)
    private boolean vacinado;

    @Column(nullable = false)
    private boolean castrado;

    @Column(nullable = false)
    private boolean vermifugado;

    @Column(length = 1000)
    private String observacoesSaude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAnimal status = StatusAnimal.DISPONIVEL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadastrado_por_id")
    private Usuario cadastradoPor;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public String getIdadeTexto() {
        if (idadeMeses < 12) {
            return idadeMeses + (idadeMeses == 1 ? " mês" : " meses");
        }
        int anos = idadeMeses / 12;
        int meses = idadeMeses % 12;
        String a = anos + (anos == 1 ? " ano" : " anos");
        if (meses == 0) return a;
        return a + " e " + meses + (meses == 1 ? " mês" : " meses");
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public Porte getPorte() { return porte; }
    public void setPorte(Porte porte) { this.porte = porte; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public int getIdadeMeses() { return idadeMeses; }
    public void setIdadeMeses(int idadeMeses) { this.idadeMeses = idadeMeses; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public boolean isConvivePets() { return convivePets; }
    public void setConvivePets(boolean convivePets) { this.convivePets = convivePets; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public boolean isVacinado() { return vacinado; }
    public void setVacinado(boolean vacinado) { this.vacinado = vacinado; }
    public boolean isCastrado() { return castrado; }
    public void setCastrado(boolean castrado) { this.castrado = castrado; }
    public boolean isVermifugado() { return vermifugado; }
    public void setVermifugado(boolean vermifugado) { this.vermifugado = vermifugado; }
    public String getObservacoesSaude() { return observacoesSaude; }
    public void setObservacoesSaude(String observacoesSaude) { this.observacoesSaude = observacoesSaude; }
    public StatusAnimal getStatus() { return status; }
    public void setStatus(StatusAnimal status) { this.status = status; }
    public Usuario getCadastradoPor() { return cadastradoPor; }
    public void setCadastradoPor(Usuario cadastradoPor) { this.cadastradoPor = cadastradoPor; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
