package org.example;

import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Inovacao {
    String nomeUser;
    String listarDiretorio;
    String senha;

    public Inovacao(String nomeUser, String listarDiretorio, String senha) {
        this.nomeUser = nomeUser;
        this.listarDiretorio = listarDiretorio;
        this.senha = senha;
    }

    public Inovacao() {
    }

    public void reconhecerUser(){
        try {
            String comandoBash = "whoami";
            Process comando = Runtime.getRuntime().exec(comandoBash);

            BufferedReader reader = new BufferedReader(new InputStreamReader(comando.getInputStream()));
            String terminal;

            while ((terminal = reader.readLine()) != null) {
                setNomeUser(terminal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String listarDiretorioMidia(){
        try {
            String comandoBash = """
                        ls /media/%s/""".formatted(nomeUser);

            Process comando = Runtime.getRuntime().exec(comandoBash);

            BufferedReader reader = new BufferedReader(new InputStreamReader(comando.getInputStream()));
            String terminal;

            while ((terminal = reader.readLine()) != null) {
                setListarDiretorio(terminal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listarDiretorio;
    }



    public String setarSenha(){
        Scanner ScannerSenha = new Scanner(System.in);

        System.out.println("forneça sua Senha");
        String senha = ScannerSenha.next();
        setSenha(senha);

        return senha;
    }

    public void ejetarUsb(){
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                String comando = "echo %s | sudo -S eject \"/media/%s/%s\"".formatted(senha, nomeUser, listarDiretorio);
                String[] comandoExecutar = {"/bin/bash", "-c", comando};

                try {
                    reconhecerUser();
                    listarDiretorioMidia();

                    Process process = Runtime.getRuntime().exec(comandoExecutar);

                    int saida = process.waitFor();
                    if (saida == 0){
                        System.out.println("o USB foi ejetado com sucesso");
                    }
                } catch (IOException e) {
                    System.out.println("Erro na ejeção: " + e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            };
        timer.schedule(tarefa, 1000, 5000);
    }


    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getListarDiretorio() {
        return listarDiretorio;
    }

    public void setListarDiretorio(String listarDiretorio) {
        this.listarDiretorio = listarDiretorio;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
