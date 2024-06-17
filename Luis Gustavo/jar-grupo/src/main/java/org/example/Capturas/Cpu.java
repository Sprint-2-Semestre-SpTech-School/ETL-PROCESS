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

        try {
            String queryInfoHardware = "INSERT INTO InfoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Cpu' sql" + e.getMessage());
        }
    }

    @Override
    public void inserirDados() {

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

                    if (looca.getProcessador().getUso() >= 70 && looca.getProcessador().getUso() < 85) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: A SUA CPU, HARDWARE NUMERO " + fkHardware + " ESTÁ COMEÇANDO A OPERAR COM UMA PORCENTAGEM DE USO ACIM DE 70%");
                            Slack.sendMessage(json);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else if (looca.getProcessador().getUso() >= 85) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: A SUA CPU, HARDWARE NUMERO " + fkHardware + " ESTÁ COMEÇANDO A OPERAR COM UMA PORCENTAGEM DE USO ACIMA DE 85%");
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
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Cpu' mysql" + e.getMessage());
        }
    }
}

