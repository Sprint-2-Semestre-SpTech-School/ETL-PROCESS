package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Rede extends Hardware {
    private Double pacotesEnviadosAnterior = 0.0;
    private Double pacotesRecebidosAnterior = 0.0;
    private Integer contadorTeste = 0;
    private Boolean primeiraCaptura = true;

    public Rede(org.example.tipoHardware tipoHardware,
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

    public Rede() {
    }

    @Override
    public void capturarDados() {
        tipoHardware = org.example.tipoHardware.REDE;
        nomeHardware = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getNomeExibicao();
        unidadeCaptacao = "pacotes";
        valorTotal = null;
        fkMaquina = 500;

        try {

            String queryInfoHardware = "INSERT INTO infoHardware (tipoHardware, nomeHardware, unidadeCaptacao, valorTotal, fkMaquina)" +
                    "VALUES (?, ?, ?, ? , ?)";
            con.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
            // con02.update(queryInfoHardware, tipoHardware.getNome(), nomeHardware, unidadeCaptacao, valorTotal, fkMaquina);
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

                    String nomeRegistro = "Pacotes Enviados";

                    String queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, pacotesEnviados, fkHardware);
                    // con02.update(queryRegistro, interfaces.get(interfaceCorreta).getPacotesEnviados(), fkHardware);

                    nomeRegistro = "Pacotes Recebidos";

                    queryRegistro = "INSERT INTO registro (nomeRegistro, valorRegistro, tempoCapturas, fkHardware) " +
                            "VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
                    con.update(queryRegistro, nomeRegistro, pacotesRecebidos, fkHardware);
                    // con02.update(queryRegistro, interfaces.get(interfaceCorreta).getPacotesRecebidos(), fkHardware);
                    contadorTeste++;
                    System.out.println();
                    System.out.println();
                    System.out.println("CONTADOR: " + contadorTeste);
                }
            };
            timer.schedule(tarefa, 1000, 5000);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Rede' sql" + e.getMessage());
        }
    }
}
