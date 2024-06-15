package org.example;

public enum tipoHardware {
    CPU ("Cpu"),
    RAM ("Ram"),
    DISCO ("Disco"),
    VOLUME ("Volume)"),
    REDE ("Rede");

    private final String nome;

    tipoHardware(String nome) {
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
