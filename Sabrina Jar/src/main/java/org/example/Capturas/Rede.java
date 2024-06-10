package org.example.Capturas;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.RedeInterface;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Rede extends Hardware {
    private Double pacotesEnviadosAnterior = 0.0;
    private Double pacotesRecebidosAnterior = 0.0;
    private Integer contadorTeste = 0;
    private Boolean primeiraCaptura = true;

    public Rede(TipoHardware tipoHardware,
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

    public Rede(Integer fkMaquina){
        this.fkMaquina = fkMaquina;
    }

    public Rede() {
    }

    @Override
    public void capturarDados() {

    }

    @Override
    public void capturarDados(Integer fkMaquina) {
        tipoHardware = TipoHardware.REDE;
        nomeHardware = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getNomeExibicao();
        unidadeCaptacao = "pacotes";
        valorTotal = null;
//        fkMaquina = 500;

        try {

            String queryInfoHardware = "INSERT INTO infoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

            GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.INFO_HARDWARE.getDescricaoTabela()), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, queryInfoHardware, Modulo.CAPTURA_HARDWARE);

             try{
                 con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);

                 GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                 GeradorLog.log(TagNiveisLog.INFO, queryInfoHardware, Modulo.CAPTURA_HARDWARE);

             } catch (RuntimeException e){
                 e.getMessage();
             }

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Rede' sql" + e.getMessage());
        }
    }

    @Override
    public void inserirDados() {
        String queryIdHardware = "SELECT LAST_INSERT_ID()";
        Integer fkHardware = con.queryForObject(queryIdHardware, Integer.class); // Espera que o retorno seja inteiro

        try {
            Timer timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                @Override
                public void run() {

                    Integer indiceInterfaceComIpv4 = null;
                    List<RedeInterface> interfaces = looca.getRede().getGrupoDeInterfaces().getInterfaces();

                    for (int i = 0; i < interfaces.size(); i++) {
                        if (!interfaces.get(i).getEnderecoIpv4().isEmpty() && interfaces.get(i).getPacotesEnviados() > 0) {
                            indiceInterfaceComIpv4 = i;
                            break;
                        }
                    }
                    Integer interfaceCorreta = indiceInterfaceComIpv4;

                    Double pacotesEnviadosMomento = (double) interfaces.get(interfaceCorreta).getPacotesEnviados();
                    Double pacotesRecebidosMomento = (double) interfaces.get(interfaceCorreta).getPacotesRecebidos();

                    System.out.println("Endereço IPv4: " + interfaces.get(interfaceCorreta).getEnderecoIpv4());
                    System.out.println("Pacotes Enviados no Momento: " + pacotesEnviadosMomento);
                    System.out.println("Pacotes Recebidos no Momento: " + pacotesRecebidosMomento);

                    if (primeiraCaptura) {
                        pacotesEnviadosAnterior = pacotesEnviadosMomento;
                        pacotesRecebidosAnterior = pacotesRecebidosMomento;
                        primeiraCaptura = false;
                        return;
                    }

                    System.out.println("Pacotes Enviados Anterior: " + pacotesEnviadosAnterior);
                    System.out.println("Pacotes Recebidos Anterior: " + pacotesRecebidosAnterior);

                    double pacotesEnviados = pacotesEnviadosMomento - pacotesEnviadosAnterior;
                    double pacotesRecebidos = pacotesRecebidosMomento - pacotesRecebidosAnterior;

                    System.out.println("Pacotes Enviados variável de captura: " + pacotesEnviados);
                    System.out.println("Pacotes Recebidos variável de captura: " + pacotesRecebidos);

                    pacotesEnviadosAnterior = pacotesEnviadosMomento;
                    pacotesRecebidosAnterior = pacotesRecebidosMomento;

                    String queryRegistroServer;
                    String queryRegistro;

                    String nomeRegistro = "Pacotes Enviados";

                        queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, pacotesEnviados, fkHardware);

                    GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                    GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                    try{
                        queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, interfaces.get(interfaceCorreta).getPacotesEnviados(), fkHardware);

                        GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                        GeradorLog.log(TagNiveisLog.INFO, queryRegistroServer, Modulo.CAPTURA_HARDWARE);

                    } catch (RuntimeException e){
                        e.getMessage();
                    }

                    nomeRegistro = "Pacotes Recebidos";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, pacotesRecebidos, fkHardware);

                    GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Local DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                    GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                    try{
                        queryRegistroServer = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                                "VALUES (?, ?, SYSDATETIME(), ?)";
                        con02.update(queryRegistroServer, nomeRegistro, pacotesRecebidos, fkHardware);

                        GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.REGISTRO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
                        GeradorLog.log(TagNiveisLog.INFO, queryRegistro, Modulo.CAPTURA_HARDWARE);

                    } catch (RuntimeException e){
                        e.getMessage();
                    }

                    if(pacotesRecebidos <= 7 && pacotesRecebidos > 3){
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA AMARELO DE MONITORAMENTO: O seu " + nomeHardware + " da maquina " + fkMaquina + " Pode estar começando a funcionar fora do parametro correto");
                            Slack.sendMessage(json);
                            GeradorLog.log(TagNiveisLog.WARN, "Envio alerta Slack!", Modulo.ALERTA);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                            GeradorLog.log(TagNiveisLog.WARN, "Erro conexão Slack!", Modulo.ALERTA);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else if(pacotesRecebidos <= 3) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("text", "ALERTA VERMELHO DE MONITORAMENTO: O " + nomeHardware + " da maquina " + fkMaquina + " ESTÁ FUNCIONANDO FORA DOS PARAMETROS");
                            Slack.sendMessage(json);
                            GeradorLog.log(TagNiveisLog.WARN, "Envio alerta Slack!", Modulo.ALERTA);
                        } catch (IOException e) {
                            System.out.println("Deu ruim no slack" + e);
                            GeradorLog.log(TagNiveisLog.WARN, "Erro conexão Slack!", Modulo.ALERTA);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    contadorTeste++;
                    System.out.println();
                    System.out.println();
                    System.out.println("CONTADOR: " + contadorTeste);
                }
            };
            timer.schedule(tarefa, 1000, 5000);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Rede' sql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.WARN, "Erro de conexão: REDE", Modulo.ALERTA);
        }
    }

    @Override
    public void gerarRelatorio() {
        System.out.println("""
                O relatório de REDE possui maior relevância no contexto Extração de ETL.
                Seu monitoramento garante a estabilidade da conexão que viabiliza e sustenta a extração de dados
                de maneira ininterrupta. Garantindo um processo contínuo...
                A saúde da sua REDE em tempo real:
                REDE: %s""".formatted(looca.getRede()));
    }
}