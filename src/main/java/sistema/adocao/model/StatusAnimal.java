package sistema.adocao.model;

public enum StatusAnimal {
    DISPONIVEL("Disponível"),
    ADOTADO("Adotado");

    private final String rotulo;
    StatusAnimal(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
    public String getCss() { return name().toLowerCase().replace('_', '-'); }
}
