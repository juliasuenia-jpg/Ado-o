package sistema.adocao.model;

public enum Perfil {
    ADOTANTE("Adotante"),
    ADMINISTRADOR("Administrador");

    private final String rotulo;
    Perfil(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
}
