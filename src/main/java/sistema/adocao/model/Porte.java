package sistema.adocao.model;

public enum Porte {
    PEQUENO("Pequeno"),
    MEDIO("Médio"),
    GRANDE("Grande");

    private final String rotulo;
    Porte(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
}
