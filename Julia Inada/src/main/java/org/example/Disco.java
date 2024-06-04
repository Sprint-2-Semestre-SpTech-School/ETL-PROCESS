package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Disco extends Hardware {
    private Double bytesLeituraAnterior = 0.0;
    private Double bytesEscritaAnterior = 0.0;
    private Double leituraAnterior = 0.0;
    private Double escritaAnterior = 0.0;
    private Boolean primeiraCaptura = true; // A primeira captura não pegará o momento
    public Disco(org.example.tipoHardware tipoHardware,
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

    public Disco() {

    }

    @Override
    public void capturarDados() {
        tipoHardware = org.example.tipoHardware.DISCO;
        nomeHardware = looca.getGrupoDeDiscos().getDiscos().get(0).getModelo();
        unidadeCaptacao = "Gb";
        valorTotal = (double) Math.round(looca.getGrupoDeDiscos().getDiscos().get(0).getTamanho() / 1e9);
        fkMaquina = 500;

        try {
            String queryInfoHardware = "INSERT INTO infoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
            // con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

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
                    Double bytesLeituraMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getLeituras();
                    Double bytesEscritaMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeEscritas();

                    if (primeiraCaptura) {
                        bytesLeituraAnterior = bytesLeituraMomento;
                        bytesEscritaAnterior = bytesEscritaMomento;
                        leituraAnterior = bytesLeituraMomento;
                        escritaAnterior = bytesEscritaAnterior;
                        primeiraCaptura = false;
                        return;
                    }

                    double bytesTransferenciaLeitura = bytesLeituraMomento - bytesLeituraAnterior;
                    double bytesTransferenciaEscrita = bytesEscritaMomento - bytesEscritaAnterior;

                    bytesLeituraAnterior = bytesLeituraMomento;
                    bytesEscritaAnterior = bytesEscritaMomento;

                    // QUANTIDADE

                    Double leituraMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getLeituras();
                    Double escritaMomento = (double) looca.getGrupoDeDiscos().getDiscos().get(0).getEscritas();

                    Double transferenciaLeitura = leituraMomento - leituraAnterior;
                    Double transferenciaEscrita = escritaMomento - escritaAnterior;

                    leituraAnterior = leituraMomento;
                    escritaAnterior = escritaMomento;

                    String nomeRegistro = "bytesLeitura";

                    String queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, bytesTransferenciaLeitura / 1e6, fkHardware);
                    // con02.update(queryRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeLeitura(), fkHardware);
                    System.out.println(bytesTransferenciaLeitura / 1e9);

                    nomeRegistro = "BytesEscrita";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, bytesTransferenciaEscrita / 1e6, fkHardware);
                    // con02.update(queryRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeEscritas(), fkHardware);

                    nomeRegistro = "leituras";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, transferenciaLeitura, fkHardware);
                    // con02.update(queryRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getLeituras(), fkHardware);

                    nomeRegistro = "escritas";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, transferenciaEscrita, fkHardware);
                    // con02.update(queryRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getEscritas(), fkHardware);

                    nomeRegistro = "tempo de transferência";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getTempoDeTransferencia() / 1000, fkHardware);
                    //con02.update(queryRegistro, looca.getGrupoDeDiscos().getDiscos().get(0).getTempoDeTransferencia(), fkHardware);

                    nomeRegistro = "memoriaDisponivel";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 1e9, fkHardware);
                    // con02.update(queryRegistro, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel(), fkHardware);

                }
            };
            timer.schedule(tarefa, 0, 30000);
        } catch (RuntimeException e){
            System.out.println("Erro de conexão 'Disco' sql" + e.getMessage());
        }
    }
}
