package org.example;

public enum TipoHardware {
    CPU("Cpu"),
    RAM("Ram"),
    DISCO("Disco"),
    VOLUME("Volume)"),
    REDE("Rede");

    private final String nome;

    TipoHardware(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Nome: " + nome;
    }
}
