package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.Capturas.Cpu;
import org.example.Capturas.Disco;
import org.example.Capturas.Ram;
import org.example.Capturas.Rede;
import org.example.Jdbc.Conexao;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

public class Main {
    private static final Conexao conexao = new Conexao();
    private static final JdbcTemplate con = conexao.getConexaoBanco();

    public static void main(String[] args) throws IOException, InterruptedException {
        Looca looca = new Looca();
        Cpu cpu = new Cpu();
        Ram ram = new Ram();
        Disco disco = new Disco();
        Rede rede = new Rede();

        Login validarLogin = new Login();
        validarLogin.validacaoLogin();
        System.out.println(validarLogin);

        if (!looca.getSistema().getSistemaOperacional().equalsIgnoreCase("Windows")) {
            Inovacao testeInova = new Inovacao();
            testeInova.setarSenha();
            testeInova.ejetarUsb();
        }

        try {
            JSONObject json = new JSONObject();
            json.put("text", "Login feito no JAVA " + "Teste para saber se eu posso dividir");
            Slack.sendMessage(json);
        } catch (IOException e) {
            System.out.println("Deu ruim no slack" + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Maquina maquina = new Maquina();

        System.out.println("Função" + maquina.consultarUsuarioPorId());


        if (maquina.consultarUsuarioPorId() != null) {
            System.out.println("É necessário ter uma máquina associada ao projeto. Por favor insira-a no website");
            System.exit(0);
//        } else if (maquina.consultarUsuarioPorId().equals(System.getProperty("user.name"))){
        } else {
            Integer idMaquina = maquina.consultarId();
            Integer idProjeto = maquina.consultarProjeto();
            Integer idEmpresa = maquina.consultarEmpresa();

            String queryVerificarTipoHardwareExiste = "SELECT COUNT(*) FROM infoHardware Where fkMaquina = %d".formatted(idMaquina);
            Integer contador = con.queryForObject(queryVerificarTipoHardwareExiste, Integer.class);

            if (contador == 0) {
                maquina.inserirDadosMaquina(idProjeto, idEmpresa);

                cpu.capturarDados(idMaquina);

                ram.capturarDados(idMaquina);

                disco.capturarDados(idMaquina);

                rede.capturarDados(idMaquina);

//                String queryIdHardware = "SELECT idHardware from infoHardware where fkMaquina = %d"
//                        .formatted(idMaquina);
//                Integer idHardware = con.queryForObject(queryIdHardware, Integer.class);

                cpu.inserirDados();
                ram.inserirDados();
                disco.inserirDados();
                rede.inserirDados();

            } else {
                cpu.inserirDados();
                ram.inserirDados();
                disco.inserirDados();
                rede.inserirDados();
            }
        }


//        if(){
//            maquina = new Maquina(400, 1)
//            Maquina maquinaCriar = new Maquina(400, 1);
//            maquinaCriar.capturarDadosMaquina();
//        }


//        maquina.inserirDadosMaquina();


//        Integer fkMaquina = maquina.consultarId();
//        System.out.println("fkMaquina: " + fkMaquina);
//        System.out.println(maquina);

//        Cpu cpu = new Cpu(fkMaquina); // Isso tem que ficar em baixo
//        cpu.capturarDados();
//        cpu.inserirDados();
//
//        Ram ram = new Ram();
//        ram.capturarDados();
//        ram.inserirDados();
//
//        Disco disco = new Disco();
//        disco.capturarDados();
//        disco.inserirDados();
//
//        Rede rede = new Rede();
//        rede.capturarDados();
//        rede.inserirDados();


            // METODO PARA INSTANCIA, CRIAR E PLOTAR O ALERTA NO SLACK:
//        if(temperatura > metrica) {
            try {
                JSONObject json = new JSONObject();
                json.put("text", "Aqui colocaremos os alertas!!");
                Slack.sendMessage(json);
            } catch (IOException e) {
                System.out.println("Deu ruim no slack" + e);
            }
        }
    }

