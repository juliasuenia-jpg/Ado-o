package sistema.adocao.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** Questionário de responsabilidade preenchido antes do agendamento da visita. */
public class SolicitacaoForm {
    private String tipoMoradia;
    private boolean possuiQuintal;
    private boolean moradiaPermiteAnimais;
    private boolean outrosAnimais;
    private String descricaoOutrosAnimais;
    private boolean experiencia;
    private Integer horasSozinho;
    private boolean todosConcordam;
    private String motivo;
    private boolean aceitaTermo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataVisita;

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
    public Integer getHorasSozinho() { return horasSozinho; }
    public void setHorasSozinho(Integer horasSozinho) { this.horasSozinho = horasSozinho; }
    public boolean isTodosConcordam() { return todosConcordam; }
    public void setTodosConcordam(boolean todosConcordam) { this.todosConcordam = todosConcordam; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public boolean isAceitaTermo() { return aceitaTermo; }
    public void setAceitaTermo(boolean aceitaTermo) { this.aceitaTermo = aceitaTermo; }
    public LocalDate getDataVisita() { return dataVisita; }
    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
}
