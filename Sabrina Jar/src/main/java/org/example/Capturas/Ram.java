package org.example.Capturas;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.example.Maquina;
import org.example.Slack;
import org.example.TipoHardware;
import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Ram extends Hardware {

    public Ram(TipoHardware tipoHardware,
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

    public Ram(Integer fkMaquina){
        this.fkMaquina = fkMaquina;
    }

    public Ram() {
    }

    @Override
    public void capturarDados() {

    }

    @Override
    public void capturarDados(Integer fkMaquina) {
        Maquina maquina = new Maquina();

        tipoHardware = TipoHardware.RAM;
        nomeHardware = null;
        unidadeCaptacao = "Gb";
        valorTotal = (double) Math.round(looca.getMemoria().getTotal() / 1e9);
//        fkMaquina = 500;

        String queryInfoHardware = "INSERT INTO infoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                "VALUES (?, ?, ?, ? , ?)";
        con.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

        GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ENVIO_DADOS);
        GeradorLog.log(TagNiveisLog.INFO, queryInfoHardware, Modulo.CAPTURA_HARDWARE);

        try{
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

            GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, queryInfoHardware, Modulo.CAPTURA_HARDWARE);

        } catch (RuntimeException e) {
            e.getMessage();
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão MYSQL Local / Server", Modulo.ALERTA);
        }

    }

    @Override
    public void inserirDados() {
        String queryIdHardware = "SELECT LAST_INSERT_ID()";
        Integer fkHardware = con.queryForObject(queryIdHardware, Integer.class);

        String nomeRegistro = "usoRam";

        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                String queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                        "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                con.update(queryRegistro, nomeRegistro, (looca.getMemoria().getEmUso() / 1e9) * 100 / valorTotal, fkHardware);

                GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                try {
                    con02.update(queryRegistro, nomeRegistro, (looca.getMemoria().getEmUso() / 1e9) * 100 / valorTotal, fkHardware);

                    GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                    GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                } catch (RuntimeException e){
                    e.getMessage();
                    GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão: RAM", Modulo.ALERTA);
                }

                if(looca.getMemoria().getEmUso() >= 70 && looca.getMemoria().getEmUso() < 85){
                    try {
                        JSONObject json = new JSONObject();
                        json.put("text", "ALERTA AMARELO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " Pode estar começando a funcionar fora do parametro correto");
                        Slack.sendMessage(json);
                        GeradorLog.log(TagNiveisLog.WARN,"Envio alerta Slack!", Modulo.ALERTA);
                    } catch (IOException e) {
                        System.out.println("Deu ruim no slack" + e);
                        GeradorLog.log(TagNiveisLog.ERROR, "Erro conexão Slack!", Modulo.ALERTA);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else if(looca.getMemoria().getEmUso() >= 80) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: O " + nomeHardware + " da maquina " + fkMaquina + " ESTÁ FUNCIONANDO FORA DOS PARAMETROS");
                        Slack.sendMessage(json);
                        GeradorLog.log(TagNiveisLog.WARN, "Envio alerta Slack!", Modulo.ALERTA);
                    } catch (IOException e) {
                        System.out.println("Deu ruim no slack" + e);
                        GeradorLog.log(TagNiveisLog.WARN, "Erro de conexão Slack!", Modulo.ALERTA);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
        timer.schedule(tarefa, 1000, 5000);
    }
}
