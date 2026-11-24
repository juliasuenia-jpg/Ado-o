package sistema.adocao.model;

public enum FaixaEtaria {
    FILHOTE("Filhote (até 1 ano)"),
    ADULTO("Adulto (1 a 7 anos)"),
    IDOSO("Idoso (8 anos ou mais)");

    private final String rotulo;
    FaixaEtaria(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
}
