package org.example.Capturas;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.example.Slack;
import org.example.TipoHardware;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Cpu extends Hardware {
    public Cpu(TipoHardware tipoHardware,
               String nomeHardware,
               String unidadeCaptacao,
               Double valorTotal,
               Integer fkMaquina,
               Looca looca,
               Conexao conexao,
               ConexaoServer conexao02,
               JdbcTemplate con,
               JdbcTemplate con02) {
        super(tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina, looca, conexao, conexao02, con, con02);
    }

    public Cpu(Integer fkMaquina){
        this.fkMaquina = fkMaquina;
    }

    public Cpu() {
    }

    @Override
    public void capturarDados() {

    }

    @Override
    public void capturarDados(Integer fkMaquina) {

        tipoHardware = TipoHardware.CPU;
        nomeHardware = looca.getProcessador().getNome();
        unidadeCaptacao = "%";
        valorTotal = (double) looca.getProcessador().getFrequencia() / 1e9;
//        fkMaquina = 500;

    try {
        JdbcTemplate con = conexao.getConexaoBanco();


        String queryInfoHardware = "INSERT INTO infoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                "VALUES (?, ?, ?, ? , ?)";
        con.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

        try{
            JdbcTemplate con02 = conexao02.getConexaoBanco();
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

//        GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data Local/MySQL DB: Table: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ENVIO_DADOS);

    }catch (RuntimeException e){
        System.out.println("Erro de conexão 'Cpu' sql" + e.getMessage());
//        GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ALERTA);
    }
    }

    @Override
    public void inserirDados() {
        try {
            String nomeRegistro = "usoCpu";

            String queryIdHardware = "SELECT LAST_INSERT_ID()";
            Integer fkHardware = con.queryForObject(queryIdHardware, Integer.class); // Espera que o retorno seja inteiro

            Timer timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                @Override
                public void run() {

                    String queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, looca.getProcessador().getUso(), fkHardware);
                    try {
                        con02.update(queryRegistro, nomeRegistro, looca.getProcessador().getUso(), fkHardware);
                    } catch (RuntimeException e){
                        System.out.println("Erro de Conexão sql Server" + e.getMessage());
                    }

                    if(looca.getProcessador().getUso() >= 70 && looca.getProcessador().getUso() < 85){
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " Pode estar começando a funcionar fora do parametro correto");
                            Slack.sendMessage(json);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else if(looca.getProcessador().getUso() >= 80) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " ESTÁ FUNCIONANDO FORA DOS PARAMETROS");
                            Slack.sendMessage(json);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            timer.schedule(tarefa, 1000, 5000);
        }catch (RuntimeException e){
            System.out.println("Erro de conexão 'Cpu' mysql" + e.getMessage());
        }
    }
}

