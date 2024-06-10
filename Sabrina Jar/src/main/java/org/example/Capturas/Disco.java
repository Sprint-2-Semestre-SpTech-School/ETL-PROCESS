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

    public Disco(Integer fkMaquina){
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
            String queryInfoHardware = "INSERT INTO infoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
            try{
                con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
            }catch (RuntimeException e){
                System.out.println(e.getMessage());
            }

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Disco' sql" + e.getMessage());
        }
    }
    @Override
    public void inserirDados() {
        String queryIdHardware = "SELECT LAST_INSERT_ID()";
        Integer fkHardware = con.queryForObject(queryIdHardware, Integer.class);

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

                    String queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, bytesTransferenciaLeitura / 1e6, fkHardware);

                    String queryRegistroServer;

                    try{
                         queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeLeitura(), fkHardware);
                        System.out.println(bytesTransferenciaLeitura / 1e9);
                    }catch (RuntimeException e){
                        System.out.println(e.getMessage());
                    }

                    nomeRegistro = "bytesEscrita";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, bytesTransferenciaEscrita / 1e6, fkHardware);

                    try {
                        queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, bytesTransferenciaEscrita / 1e6, fkHardware);
                    } catch (RuntimeException e){
                        e.getMessage();
                    }

                    if(bytesTransferenciaEscrita <= 2 && bytesTransferenciaEscrita > 1){
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " Pode estar começando a funcionar fora do parametro correto");
                            Slack.sendMessage(json);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(bytesTransferenciaEscrita <= 1) {
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

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, transferenciaLeitura, fkHardware);

                    try {
                        queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, transferenciaLeitura, fkHardware);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                    }

                    nomeRegistro = "escritas";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, transferenciaEscrita, fkHardware);

                    try {
                        queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, transferenciaEscrita, fkHardware);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                    }

                    nomeRegistro = "tempo de transferência";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, tempoTransferencia / 1000, fkHardware);

                    try {
                        queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, tempoTransferencia / 1000, fkHardware);
                    } catch (RuntimeException e){
                        System.out.println(e.getMessage());
                    }


                    nomeRegistro = "memoriaDisponivel";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 1e9, fkHardware);

                    try {
                        queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistro, nomeRegistro, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 1e9, fkHardware);
                    } catch (RuntimeException e){
                        e.getMessage();
                    }
                }
            };
            timer.schedule(tarefa, 1000, 5000);
        } catch (RuntimeException e){
            System.out.println("Erro de conexão 'Disco' sql" + e.getMessage());
        }
    }
}
