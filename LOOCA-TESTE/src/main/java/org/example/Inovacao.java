package org.example;

import com.github.britooo.looca.api.core.Looca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class Inovacao {

    String nomeUser;
    String listarDiretorio;

    public Inovacao(String nomeUser, String listarDiretorio) {
        this.nomeUser = nomeUser;
        this.listarDiretorio = listarDiretorio;
    }
    public Inovacao(){

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
                System.out.println(line);
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

//        System.out.println(getNomeUser());

        while(comandoRetorna = true){
            try {
////                 Comando que deseja executar
                String comandoBash = """
                        ls /media/%s/""".formatted(nomeUser);

//                String comandoBash2 = "ls /media/arthur/";

                Process process = Runtime.getRuntime().exec(comandoBash);

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    setListarDiretorio(line);
                    System.out.println(line);
                    comandoRetorna = true;
                }
                int exitCode = process.waitFor();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(nomeUser);

        return listarDiretorio;
    }




    public void ejetarUSB(){

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
