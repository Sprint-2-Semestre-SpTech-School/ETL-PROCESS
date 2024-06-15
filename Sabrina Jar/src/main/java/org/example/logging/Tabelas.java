package org.example.logging;

public enum Tabelas {
    EMPRESA("Empresa"),
    LOCALIZACAO_EMPRESA("localizacaoEmpresa"),
    CONTATO("Contato"),
    CONTA("Conta"),
    PROJETO("Projeto"),
    MAQUINA("Máquina"),
    REGISTRO("registro"),
    DISPOSITIVO_USB("dispositivosUsb"),
    BLACK_LIST("blackList"),
    INFO_HARDWARE("infoHardware");

    private final String descricaoTabela;

    Tabelas(String descricaoTabela) {
        this.descricaoTabela = descricaoTabela;
    }

    public String getDescricaoTabela() {
        return descricaoTabela;
    }
}
