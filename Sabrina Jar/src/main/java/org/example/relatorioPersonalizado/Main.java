package org.example.relatorioPersonalizado;

import org.example.Capturas.Cpu;
import org.example.Capturas.Disco;
import org.example.Capturas.Ram;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scam = new Scanner(System.in);
        int userOption;
        Cpu cpu = new Cpu();
        Disco disco = new Disco();
        Ram ram = new Ram();
        SimpleDateFormat formatDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = formatDateTime.format(new Date());

        System.out.println("Seja bem-vindo(a)!");

        do {
            System.out.println("Escolha uma opção para exibir seu relatório diário personalizado:");
            for(OpcoesRelatorio opcao : OpcoesRelatorio.values()){
                System.out.println((opcao.ordinal() + 1) + " - " + opcao.getDescricao());
            }
            userOption = scam.nextInt();

        }while (userOption < 1 || userOption > OpcoesRelatorio.values().length);

        OpcoesRelatorio option = OpcoesRelatorio.values()[userOption - 1];

        switch (option) {
            case DISCO -> {
                System.out.println(option.getDescricao() + " - " + currentDateTime);
//                disco.gerarRelatorio();
            }
            case CPU -> {
                System.out.println(option.getDescricao());
//                cpu.exibirRelatiorio;
            }
            case RAM -> {
                System.out.println(option.getDescricao());
//                ram.exibirRelatorio;
            }
            case SAIR -> {
                System.out.println(option.getDescricao());
                System.exit(0);
            }
        }
    }
}
