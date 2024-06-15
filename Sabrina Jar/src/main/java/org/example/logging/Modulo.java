package org.example.logging;

public enum Modulo {
    CAPTURA_HARDWARE("M001"),
    PROCESSAMENTO_DADOS("M002"),
    ENVIO_DADOS("M003"),
    AUTENTICACAO("M004"),
    ALERTA("M005"),
    GERAL("M006");

    private final String ID;

    Modulo(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }
}
