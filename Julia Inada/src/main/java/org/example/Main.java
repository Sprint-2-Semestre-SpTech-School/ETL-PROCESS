package org.example;

import org.checkerframework.checker.units.qual.C;

public class Main {
    public static void main(String[] args) {
        Cpu cpu01 = new Cpu();
        cpu01.obterFabricante();

      EscolhaEtapa etapa01 = new EscolhaEtapa();
      etapa01.escolherProcesso();
//      etapa01.validarProcessos();

    }
}
