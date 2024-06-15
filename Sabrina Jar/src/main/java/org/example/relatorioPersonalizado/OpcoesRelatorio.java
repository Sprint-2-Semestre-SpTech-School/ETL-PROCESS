package org.example.relatorioPersonalizado;

public enum OpcoesRelatorio {
    DISCO("Relatório de DISCO"),
    CPU("Relatório de CPU"),
    RAM("Relatório de RAM"),
    SAIR("Sair");

    private final String descricao;

    OpcoesRelatorio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
