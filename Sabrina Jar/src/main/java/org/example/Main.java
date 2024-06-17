package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.Capturas.Cpu;
import org.example.Capturas.Disco;
import org.example.Capturas.Ram;
import org.example.Capturas.Rede;
import org.example.Jdbc.ConexaoServer;
import org.example.logging.GeradorLog;
import org.example.relatorioLogs.GeradorResumo;
import org.example.logging.Modulo;
import org.example.logging.TagNiveisLog;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

public class Main {
//    private static final Conexao conexao = new Conexao();
    private static final ConexaoServer conexao = new ConexaoServer();
//    private static final JdbcTemplate con = conexao.getConexaoBanco();
    private static final JdbcTemplate con02 = conexao.getConexaoBanco();

    public static void main(String[] args) throws IOException, InterruptedException {
        Looca looca = new Looca();
        Cpu cpu = new Cpu();
        Ram ram = new Ram();
        Disco disco = new Disco();
        Rede rede = new Rede();

        GeradorLog.log(TagNiveisLog.INFO, "Iniciando aplicação de captura...", Modulo.GERAL);

        Login validarLogin = new Login();
        validarLogin.validacaoLogin();

        if (!looca.getSistema().getSistemaOperacional().equalsIgnoreCase("Windows")) { // Inovação Linux
            Inovacao testeInova = new Inovacao();
            testeInova.setarSenha();
            testeInova.ejetarUsb();
        }

        try {
            JSONObject json = new JSONObject();
            json.put("text", "Foi realizado um Login no JAVA");
            GeradorLog.log(TagNiveisLog.INFO, "Autenticação confirmada via Slack", Modulo.GERAL);
            Slack.sendMessage(json);
        } catch (IOException e) {
            System.out.println("Falhas de conexão com Slack" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Tentativa falha de envio de mensagem no Slack!", Modulo.GERAL);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Maquina maquina = new Maquina();

        if (maquina.consultarUsuarioPorId() == null) {
            Integer idProjeto = maquina.consultarProjeto();
            Integer idEmpresa = maquina.consultarEmpresa();
            Integer idMaquina = maquina.consultarId();

            maquina.capturarDadosMaquina();
            maquina.inserirDadosMaquina(idProjeto, idEmpresa);

            cpu.capturarDados(idMaquina);

            ram.capturarDados(idMaquina);

            disco.capturarDados(idMaquina);

            rede.capturarDados(idMaquina);

            Integer idHardwareCpu = maquina.consultarHardwareCpu();
            Integer idHardwareRam = maquina.consultarHardwareRam();
            Integer idHardwareDisco = maquina.consultarHardwareDisco();
            Integer idHardwareRede = maquina.consultarHardwareRede();

            cpu.inserirDados(idHardwareCpu);
            ram.inserirDados(idHardwareRam);
            disco.inserirDados(idHardwareDisco);
            rede.inserirDados(idHardwareRede);

        } else {
            Integer idMaquina = maquina.consultarId();

            String queryVerificarTipoHardwareExiste = "SELECT COUNT(*) FROM InfoHardware Where fkMaquina = %d".formatted(idMaquina);
            Integer contador = con02.queryForObject(queryVerificarTipoHardwareExiste, Integer.class);

            if (contador == 0) {
                cpu.capturarDados(idMaquina);

                ram.capturarDados(idMaquina);

                disco.capturarDados(idMaquina);

                rede.capturarDados(idMaquina);

                // CONSULTANDO IDS PRA INSERIR NO HARDAWRE CORRETO
                Integer idHardwareCpu = maquina.consultarHardwareCpu();
                Integer idHardwareRam = maquina.consultarHardwareRam();
                Integer idHardwareDisco = maquina.consultarHardwareDisco();
                Integer idHardwareRede = maquina.consultarHardwareRede();

                cpu.inserirDados(idHardwareCpu);
                ram.inserirDados(idHardwareRam);
                disco.inserirDados(idHardwareDisco);
                rede.inserirDados(idHardwareRede);

            } else {
                // CONSULTANDO IDS PRA INSERIR NO HARDAWRE CORRETO
                Integer idHardwareCpu = maquina.consultarHardwareCpu();
                Integer idHardwareRam = maquina.consultarHardwareRam();
                Integer idHardwareDisco = maquina.consultarHardwareDisco();
                Integer idHardwareRede = maquina.consultarHardwareRede();

                cpu.inserirDados(idHardwareCpu);
                ram.inserirDados(idHardwareRam);
                disco.inserirDados(idHardwareDisco);
                rede.inserirDados(idHardwareRede);
            }
        }
        GeradorResumo.gerarArquivoResumo();
        GeradorLog.autoClean(0, 1440);
        GeradorLog.cleanerOldLogs(6);
    }
}

