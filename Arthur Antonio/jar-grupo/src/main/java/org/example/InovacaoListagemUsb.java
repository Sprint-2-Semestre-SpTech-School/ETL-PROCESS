package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InovacaoListagemUsb {
    String nomeUser;
    String listarDiretorio;

    public InovacaoListagemUsb(String nomeUser, String listarDiretorio) {
        this.nomeUser = nomeUser;
        this.listarDiretorio = listarDiretorio;
    }

    public InovacaoListagemUsb() {

    }

    public String reconhecerUser(){

        try {
            String comandoBash = "whoami";

            // Inicia o processo
            Process process = Runtime.getRuntime().exec(comandoBash);

            // Lê a saída do processo
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                setNomeUser(line);
            }
            // Espera pelo término do processo
            int exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(nomeUser);
        return nomeUser;
    }

    public String listarDiretorioMidia(){
        boolean comandoRetorna = false;

        reconhecerUser();

        while(comandoRetorna == false){
            try {
                String comandoBash = """
                        ls /media/%s/""".formatted(nomeUser);

                String comandoBash2 = "ls /media/arthur/";

                Process process = Runtime.getRuntime().exec(comandoBash);


                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    setListarDiretorio(line);
                    System.out.println(line);
                    comandoRetorna = true;
                    System.out.println("O nome do pen-drive acima me permite ejeta-lo via terminal de comando!!");
                }
                int exitCode = process.waitFor();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        listarDiretorioMidia();
        return listarDiretorio;
    }

    public void ejetarUSB() {


        try {

            String comandoBash = """
                        ls /media/%s/""".formatted(nomeUser);

            Process process = Runtime.getRuntime().exec(comandoBash);

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                setListarDiretorio(line);
                System.out.println(line);
            }
            int exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

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
}
