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

    public Cpu(Integer fkMaquina) {
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
        unidadeCaptacao = "Ghz";
        valorTotal = (double) looca.getProcessador().getFrequencia() / 1e9;

        GeradorLog.log(TagNiveisLog.INFO, "Iniciando captura de dados do Hardware: " + TipoHardware.CPU, Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Descrição: %s".formatted(nomeHardware), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Unidade captura: %s".formatted(unidadeCaptacao), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Valor total: %.2f".formatted(valorTotal), Modulo.CAPTURA_HARDWARE);

        try {
            String queryInfoHardware = "INSERT INTO InfoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

            GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! -> Re;Data SQL Server DB / Tabela " + Tabelas.INFO_HARDWARE, Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "Hardware: %s".formatted(tipoHardware), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "Descrição: %s".formatted(nomeHardware), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "Unidade de captura: %s".formatted(unidadeCaptacao), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "Valor total: %.2f".formatted(valorTotal), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "Máquina referida: %s".formatted(fkMaquina), Modulo.ENVIO_DADOS);

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Cpu' sql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL / Tabela " + Tabelas.INFO_HARDWARE, Modulo.ENVIO_DADOS);
        }
    }

    @Override
    public void inserirDados(Integer fkHardware) {
        try {
            String nomeRegistro = "usoCpu";

            Timer timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                @Override
                public void run() {

                    String queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, looca.getProcessador().getUso(), fkHardware);

                    GeradorLog.log(TagNiveisLog.INFO, "Novo registro capturado! -> Tabela: " + Tabelas.REGISTRO, Modulo.ALERTA);
                    GeradorLog.log(TagNiveisLog.INFO, "Nome: %s".formatted(nomeRegistro), Modulo.ENVIO_DADOS);
                    GeradorLog.log(TagNiveisLog.INFO, "Valor capturado: " + looca.getProcessador().getUso(), Modulo.ENVIO_DADOS);
                    GeradorLog.log(TagNiveisLog.INFO, "ID Hardware: %d".formatted(fkHardware), Modulo.ENVIO_DADOS);

                    if (looca.getProcessador().getUso() >= 70 && looca.getProcessador().getUso() < 85) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: A SUA CPU, HARDWARE NUMERO " + fkHardware + " ESTÁ COMEÇANDO A OPERAR COM UMA PORCENTAGEM DE USO ACIM DE 70%");
                            Slack.sendMessage(json);
                            GeradorLog.log(TagNiveisLog.WARN, "Alerta amarelo de monitoramento via Slack", Modulo.ALERTA);
                            GeradorLog.log(TagNiveisLog.WARN, "Alteração nos indicadores do processador!", Modulo.ALERTA);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão com Slack!", Modulo.ALERTA);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else if (looca.getProcessador().getUso() >= 85) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: A SUA CPU, HARDWARE NUMERO " + fkHardware + " ESTÁ COMEÇANDO A OPERAR COM UMA PORCENTAGEM DE USO ACIMA DE 85%");
                            Slack.sendMessage(json);
                            GeradorLog.log(TagNiveisLog.WARN, "Alerta VERMELHO de monitoramento via Slack", Modulo.ALERTA);
                            GeradorLog.log(TagNiveisLog.WARN, "Alteração precupante nos indicadores do processador!", Modulo.ALERTA);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão com Slack!", Modulo.ALERTA);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            timer.schedule(tarefa, 1000, 5000);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Cpu' mysql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL em CPU", Modulo.ALERTA);
        }
    }

    @Override
    public void inserirDados() {

    }
}

