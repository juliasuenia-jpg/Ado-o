package sistema.adocao.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "adotante_id", nullable = false)
    private Usuario adotante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacao status = StatusSolicitacao.PENDENTE;

    // ---- Questionário de triagem ----
    @Column(nullable = false, length = 30)
    private String tipoMoradia;

    @Column(nullable = false)
    private boolean possuiQuintal;

    @Column(nullable = false)
    private boolean moradiaPermiteAnimais;

    @Column(nullable = false)
    private boolean outrosAnimais;

    @Column(length = 300)
    private String descricaoOutrosAnimais;

    @Column(nullable = false)
    private boolean experiencia;

    @Column(nullable = false)
    private int horasSozinho;

    @Column(nullable = false)
    private boolean todosConcordam;

    @Column(nullable = false, length = 1000)
    private String motivo;

    @Column(nullable = false)
    private boolean aceitaTermo;

    /** Pontuação automática de 0 a 100 usada como apoio na triagem inicial. */
    @Column(nullable = false)
    private int compatibilidade;

    @Column(nullable = false)
    private LocalDate dataVisita;

    /** Resposta/parecer da ONG para o adotante. */
    @Column(length = 1000)
    private String parecerOng;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    private LocalDateTime atualizadoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    public Usuario getAdotante() { return adotante; }
    public void setAdotante(Usuario adotante) { this.adotante = adotante; }
    public StatusSolicitacao getStatus() { return status; }
    public void setStatus(StatusSolicitacao status) { this.status = status; }
    public String getTipoMoradia() { return tipoMoradia; }
    public void setTipoMoradia(String tipoMoradia) { this.tipoMoradia = tipoMoradia; }
    public boolean isPossuiQuintal() { return possuiQuintal; }
    public void setPossuiQuintal(boolean possuiQuintal) { this.possuiQuintal = possuiQuintal; }
    public boolean isMoradiaPermiteAnimais() { return moradiaPermiteAnimais; }
    public void setMoradiaPermiteAnimais(boolean v) { this.moradiaPermiteAnimais = v; }
    public boolean isOutrosAnimais() { return outrosAnimais; }
    public void setOutrosAnimais(boolean outrosAnimais) { this.outrosAnimais = outrosAnimais; }
    public String getDescricaoOutrosAnimais() { return descricaoOutrosAnimais; }
    public void setDescricaoOutrosAnimais(String d) { this.descricaoOutrosAnimais = d; }
    public boolean isExperiencia() { return experiencia; }
    public void setExperiencia(boolean experiencia) { this.experiencia = experiencia; }
    public int getHorasSozinho() { return horasSozinho; }
    public void setHorasSozinho(int horasSozinho) { this.horasSozinho = horasSozinho; }
    public boolean isTodosConcordam() { return todosConcordam; }
    public void setTodosConcordam(boolean todosConcordam) { this.todosConcordam = todosConcordam; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public boolean isAceitaTermo() { return aceitaTermo; }
    public void setAceitaTermo(boolean aceitaTermo) { this.aceitaTermo = aceitaTermo; }
    public int getCompatibilidade() { return compatibilidade; }
    public void setCompatibilidade(int compatibilidade) { this.compatibilidade = compatibilidade; }
    public LocalDate getDataVisita() { return dataVisita; }
    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
    public String getParecerOng() { return parecerOng; }
    public void setParecerOng(String parecerOng) { this.parecerOng = parecerOng; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
