package sistema.adocao.dto;

import sistema.adocao.model.*;

/** Filtros da busca avançada de animais. Campos vazios são ignorados. */
public class AnimalFiltro {
    private Especie especie;
    private Porte porte;
    private Sexo sexo;
    private FaixaEtaria faixaEtaria;
    private String cidade;
    private Boolean convivePets;

    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public Porte getPorte() { return porte; }
    public void setPorte(Porte porte) { this.porte = porte; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public FaixaEtaria getFaixaEtaria() { return faixaEtaria; }
    public void setFaixaEtaria(FaixaEtaria faixaEtaria) { this.faixaEtaria = faixaEtaria; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public Boolean getConvivePets() { return convivePets; }
    public void setConvivePets(Boolean convivePets) { this.convivePets = convivePets; }

    public boolean isVazio() {
        return especie == null && porte == null && sexo == null && faixaEtaria == null
                && (cidade == null || cidade.isBlank()) && convivePets == null;
    }
}
