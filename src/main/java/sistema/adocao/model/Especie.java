package sistema.adocao.model;

public enum Especie {
    CACHORRO("Cachorro", "🐶"),
    GATO("Gato", "🐱"),
    OUTRO("Outro", "🐾");

    private final String rotulo;
    private final String emoji;
    Especie(String rotulo, String emoji) { this.rotulo = rotulo; this.emoji = emoji; }
    public String getRotulo() { return rotulo; }
    public String getEmoji() { return emoji; }
}
