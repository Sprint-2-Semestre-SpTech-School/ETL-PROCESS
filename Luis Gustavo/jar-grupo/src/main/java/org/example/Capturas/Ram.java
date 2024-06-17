package org.example.Capturas;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.example.Maquina;
import org.example.Slack;
import org.example.TipoHardware;
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

    public Ram(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    public Ram() {
    }

    @Override
    public void capturarDados() {

    }

    @Override
    public void capturarDados(Integer fkMaquina) {
        tipoHardware = TipoHardware.RAM;
        nomeHardware = null;
        unidadeCaptacao = "Gb";
        valorTotal = (double) Math.round(looca.getMemoria().getTotal() / 1e9);

        try {
            String queryInfoHardware = "INSERT INTO InfoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Ram' sql" + e.getMessage());
        }

    }

    @Override
    public void inserirDados() {

    }

    @Override
    public void inserirDados(Integer fkHardware) {
        String nomeRegistro = "usoRam";

        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {
                String queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                        "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                con02.update(queryRegistro, nomeRegistro, (looca.getMemoria().getEmUso() / 1e9) * 100 / (double) Math.round(looca.getMemoria().getTotal() / 1e9), fkHardware);
                try {
                    con02.update(queryRegistro, nomeRegistro, (looca.getMemoria().getEmUso() / 1e9) * 100 / (double) Math.round(looca.getMemoria().getTotal() / 1e9), fkHardware);
                } catch (RuntimeException e) {
                    e.getMessage();
                }

                if (looca.getMemoria().getEmUso() >= 70 && looca.getMemoria().getEmUso() < 85) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("text", "ALERTA AMARELO DE MONITORAMENTO: A sua RAM, hardware numero " + fkHardware + ", PODE ESTAR COMEÇANDO A FUNCIONAR COM UMA PORCETAGEM DE USO ACIMA DE 70%");
                        Slack.sendMessage(json);
                    } catch (IOException e) {
                        System.out.println("Deu ruim no slack" + e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else if (looca.getMemoria().getEmUso() >= 90) {
                    try {
                        JSONObject json = new JSONObject();
                            json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: A sua RAM da maquina do hardware" + fkHardware + " ESTÁ FUNCIONANDO COM UMA PORCETAGEM DE USO ACIMA DE 85%");
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
    }
}
