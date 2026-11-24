package sistema.adocao.model;

public enum StatusSolicitacao {
    PENDENTE("Pendente"),
    EM_ANALISE("Em análise"),
    APROVADO("Aprovado"),
    RECUSADO("Recusado");

    private final String rotulo;
    StatusSolicitacao(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
    public String getCss() { return name().toLowerCase().replace('_', '-'); }
    /** Solicitação ainda em andamento ou já aprovada (impede novo pedido do mesmo adotante). */
    public boolean isAtiva() { return this != RECUSADO; }
}
