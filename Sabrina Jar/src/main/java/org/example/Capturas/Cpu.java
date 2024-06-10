package org.example.Capturas;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
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

        GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ENVIO_DADOS);
        GeradorLog.log(TagNiveisLog.INFO, queryInfoHardware, Modulo.CAPTURA_HARDWARE);

        try{
            JdbcTemplate con02 = conexao02.getConexaoBanco();
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

            GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, queryInfoHardware, Modulo.CAPTURA_HARDWARE);

        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL Server & Local MySQL", Modulo.ALERTA);
        }

    }catch (RuntimeException e){
        System.out.println("Erro de conexão 'Cpu' sql" + e.getMessage());
        GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ALERTA);
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

                    GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                    GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                    try {
                        con02.update(queryRegistro, nomeRegistro, looca.getProcessador().getUso(), fkHardware);

                        GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                        GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                    } catch (RuntimeException e){
                        System.out.println("Erro de Conexão sql Server" + e.getMessage());
                        GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão: SQL server & MySQL Local DB", Modulo.ALERTA);
                    }

                    if(looca.getProcessador().getUso() >= 70 && looca.getProcessador().getUso() < 85){
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " Pode estar começando a funcionar fora do parametro correto");
                            Slack.sendMessage(json);
                            GeradorLog.log(TagNiveisLog.WARN, "Alerta do Slack!", Modulo.ALERTA);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                            GeradorLog.log(TagNiveisLog.ERROR, "Erro conexão Slack!", Modulo.ALERTA);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else if(looca.getProcessador().getUso() >= 80) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " ESTÁ FUNCIONANDO FORA DOS PARAMETROS");
                            Slack.sendMessage(json);
                            GeradorLog.log(TagNiveisLog.WARN, "Alerta do Slack!", Modulo.ALERTA);
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
            GeradorLog.log(TagNiveisLog.INFO, "Erro de conexão! Mysql CPU", Modulo.GERAL);
        }
    }

    @Override
    public void gerarRelatorio() {
        System.out.println("""
                O relatório de CPU possui maior relevância no contexto Transformação de ETL.
                Seu monitoramento garante um processamento de dados eficaz operando de maneira
                veloz mesmo com operações intensas de tratamento de dados.
                A saúde da sua CPU em tempo real:
                CPU: %s
                Uso: %.2f
                Frequência: %s""".formatted(looca.getProcessador().getNome(), looca.getProcessador().getUso(), looca.getProcessador().getFrequencia()));
    }
}

