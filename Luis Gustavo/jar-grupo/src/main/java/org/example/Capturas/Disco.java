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

public class Disco extends Hardware {
    private Double bytesLeituraAnterior = 0.0;
    private Double bytesEscritaAnterior = 0.0;
    private Double leituraAnterior = 0.0;
    private Double escritaAnterior = 0.0;
    private Double tempoTransferenciaAnterior = 0.0;
    private Boolean primeiraCaptura = true; // A primeira captura não pegará o momento

    public Disco(TipoHardware tipoHardware,
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

    public Disco(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    public Disco() {

    }

    @Override
    public void capturarDados() {

    }

    @Override
    public void capturarDados(Integer fkMaquina) {
        tipoHardware = TipoHardware.DISCO;
        nomeHardware = looca.getGrupoDeDiscos().getDiscos().get(0).getModelo();
        unidadeCaptacao = "Gb";
        valorTotal = (double) Math.round(looca.getGrupoDeDiscos().getDiscos().get(0).getTamanho() / 1e9);
//        this.fkMaquina = 500;

        try {
            String queryInfoHardware = "INSERT INTO InfoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Disco' sql" + e.getMessage());
        }
    }

    @Override
    public void inserirDados() {

    }

    @Override
    public void inserirDados(Integer fkHardware) {
        try {
            Timer timer = new Timer();
            TimerTask tarefa = new TimerTask() {

                @Override
                public void run() {
                    Double bytesLeituraMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeLeitura();
                    Double bytesEscritaMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeEscritas();

                    Double leituraMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getLeituras();
                    Double escritaMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getEscritas();

                    Double tempoTransferenciaMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getTempoDeTransferencia();

                    if (primeiraCaptura) {
                        bytesLeituraAnterior = bytesLeituraMomento;
                        bytesEscritaAnterior = bytesEscritaMomento;

                        leituraAnterior = leituraMomento;
                        escritaAnterior = escritaMomento;

                        tempoTransferenciaAnterior = tempoTransferenciaMomento;
                        primeiraCaptura = false;
                        return;
                    }

                    double bytesTransferenciaLeitura = bytesLeituraMomento - bytesLeituraAnterior;
                    double bytesTransferenciaEscrita = bytesEscritaMomento - bytesEscritaAnterior;

                    bytesLeituraAnterior = bytesLeituraMomento;
                    bytesEscritaAnterior = bytesEscritaMomento;

                    // QUANTIDADE

                    Double transferenciaLeitura = leituraMomento - leituraAnterior;
                    Double transferenciaEscrita = escritaMomento - escritaAnterior;

                    leituraAnterior = leituraMomento;
                    escritaAnterior = escritaMomento;

                    // Tempo Transferência

                    Double tempoTransferencia = tempoTransferenciaMomento - tempoTransferenciaAnterior;

                    tempoTransferenciaAnterior = tempoTransferenciaMomento;

                    String nomeRegistro = "bytesLeitura";

                    String queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, bytesTransferenciaLeitura / 1e6, fkHardware);

                    nomeRegistro = "bytesEscrita";

                    queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, bytesTransferenciaEscrita / 1e6, fkHardware);

                    if (bytesTransferenciaEscrita <= 2 && bytesTransferenciaEscrita > 1) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " Pode estar começando a funcionar fora do parametro correto");
                            Slack.sendMessage(json);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (bytesTransferenciaEscrita <= 1) {
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

                    nomeRegistro = "leituras";

                    queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, transferenciaLeitura, fkHardware);

                    nomeRegistro = "escritas";

                    queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, transferenciaEscrita, fkHardware);

                    nomeRegistro = "tempo de transferência";

                    queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, tempoTransferencia / 1000, fkHardware);

                    nomeRegistro = "memoriaDisponivel";

                    queryRegistro = "INSERT INTO Registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con02.update(queryRegistro, nomeRegistro, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 1e9, fkHardware);

                }
            };
            timer.schedule(tarefa, 1000, 5000);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Disco' sql" + e.getMessage());
        }
    }
}
