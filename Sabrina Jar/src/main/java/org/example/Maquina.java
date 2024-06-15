package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.ls.LSOutput;

import java.util.List;

public class Maquina {
    private static final Log log = LogFactory.getLog(Maquina.class);
    Looca looca = new Looca();
    private Integer idMaquina;
    private String usuario;
    private String sistemaOperacional;
    private Double temperatura;
    private Long tempoAtividade;
    private String destino;
    private String descricao;
    private Integer fkProjeto;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private ConexaoServer conexao02 = new ConexaoServer();
    private JdbcTemplate con = conexao.getConexaoBanco();
    private JdbcTemplate con02 = conexao02.getConexaoBanco();

    public Maquina(
            Looca looca,
            Integer idMaquina,
            String usuario,
            String sistemaOperacional,
            Double temperatura,
            Long tempoAtividade,
            String destino,
            String descricao,
            Integer fkProjeto,
            Integer fkEmpresa,
            Conexao conexao,
            ConexaoServer conexao02
    ) {
        this.looca = looca;
        this.idMaquina = idMaquina;
        this.usuario = usuario;
        this.sistemaOperacional = sistemaOperacional;
        this.temperatura = temperatura;
        this.tempoAtividade = tempoAtividade;
        this.destino = destino;
        this.descricao = descricao;
        this.fkProjeto = fkProjeto;
        this.fkEmpresa = fkEmpresa;
        this.conexao = conexao;
        this.conexao02 = conexao02;
    }

    public Maquina(Integer fkProjeto, Integer fkEmpresa) {
        this.fkProjeto = fkProjeto;
        this.fkEmpresa = fkEmpresa;
    }

    public Maquina() {

    }

    public void capturarDadosMaquina() {

        usuario = System.getProperty("user.name");
        sistemaOperacional = looca.getSistema().getSistemaOperacional();
        temperatura = looca.getTemperatura().getTemperatura();
        tempoAtividade = looca.getSistema().getTempoDeAtividade() / 3600; // Valor em horas
        GeradorLog.log(TagNiveisLog.INFO, "Iniciando captura de dados da máquina: %d".formatted(consultarId()), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Usuário: %s".formatted(usuario), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "OS: %s".formatted(sistemaOperacional), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Tempo de atividade: %.2f horas".formatted(tempoAtividade), Modulo.CAPTURA_HARDWARE);
    }

    public void inserirDadosMaquina(Integer fkProjeto, Integer fkEmpresa) {
        try {
            con02.update("UPDATE Maquina SET usuario =?, sistemaOperacional =?, temperatura =?, tempoAtividade =?, " +
                            "fkProjeto =?, fkEmpresa =?",
                    usuario, sistemaOperacional, temperatura, tempoAtividade, fkProjeto, fkEmpresa);

            GeradorLog.log(TagNiveisLog.INFO, "Dados enviados com sucesso! -> Re;Data SQL Server DB / Tabela " + Tabelas.MAQUINA, Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "Usuário: %s".formatted(usuario), Modulo.CAPTURA_HARDWARE);
            GeradorLog.log(TagNiveisLog.INFO, "OS: %s".formatted(sistemaOperacional), Modulo.CAPTURA_HARDWARE);
            GeradorLog.log(TagNiveisLog.INFO, "Tempo de atividade: %.2f horas".formatted(tempoAtividade), Modulo.CAPTURA_HARDWARE);

//            con.update("UPDATE Maquina SET usuario =?, sistemaOperacional =?, temperatura =?, tempoAtividade =?, " +
//                            "fkProjeto =?, fkEmpresa =?",
//                    usuario, sistemaOperacional, temperatura, tempoAtividade, fkProjeto, fkEmpresa);

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Maquina' sql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL / Tabela " + Tabelas.MAQUINA, Modulo.ENVIO_DADOS);
        }
    }

    //    ================================================ SERVER =====================================================
    public Integer consultarId() {
        try {
            String comandoSql = "SELECT TOP 1 idMaquina FROM Maquina ORDER BY idMaquina DESC";
            idMaquina = con02.queryForObject(comandoSql, Integer.class);
            GeradorLog.log(TagNiveisLog.INFO, "Resultado da função consultarId: %d / Tabela ".formatted(idMaquina) + Tabelas.MAQUINA, Modulo.AUTENTICACAO);
        } catch (RuntimeException e) {
            System.out.println(" Erro na consulta de ID " + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL / Tabela " + Tabelas.MAQUINA, Modulo.CAPTURA_HARDWARE);
        }
        return idMaquina;
    }

//    ================================================ LOCAL =====================================================
//    public Integer consultarId() {
//        try {
//            String comandoSql = "SELECT idMaquina from Maquina order by idMaquina DESC LIMIT 1";
//            idMaquina = con.queryForObject(comandoSql, Integer.class);
//        } catch (RuntimeException e) {
//            System.out.println(" Erro na consulta de ID " + e.getMessage());
//        }
//        return idMaquina;
//    }

//    public Integer consultarProjeto() {
//        String comandoSql = "SELECT fkProjeto from Maquina where idMaquina = %d".formatted(consultarId());
//        return fkProjeto = con.queryForObject(comandoSql, Integer.class);
//    }

    public Integer consultarProjeto() {
        String comandoSql = "SELECT fkProjeto from Maquina where idMaquina = %d".formatted(consultarId());
        GeradorLog.log(TagNiveisLog.INFO, "Resultado da função consultarProjeto: %d / Tabela ".formatted(fkProjeto) + Tabelas.MAQUINA, Modulo.AUTENTICACAO);
        return fkProjeto = con02.queryForObject(comandoSql, Integer.class);
    }

//    public Integer consultarEmpresa() {
//        String comandoSql = "SELECT fkEmpresa from Maquina where idMaquina = %d".formatted(consultarId());
//        return fkEmpresa = con.queryForObject(comandoSql, Integer.class);
//    }

    public Integer consultarEmpresa() {
        String comandoSql = "SELECT fkEmpresa from Maquina where idMaquina = %d".formatted(consultarId());
        GeradorLog.log(TagNiveisLog.INFO, "Resultado da função consultarEmpresa: %d / Tabela ".formatted(fkEmpresa) + Tabelas.MAQUINA, Modulo.AUTENTICACAO);
        return fkEmpresa = con02.queryForObject(comandoSql, Integer.class);
    }

//    public Integer consultarHardwareCpu() {
//        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
//                "where idMaquina = %d and tipoHardware = 'Cpu';".formatted(consultarId());
//        return con.queryForObject(comandoSql, Integer.class);
//    }

    public Integer consultarHardwareCpu() {
        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
                "where idMaquina = %d and tipoHardware = 'Cpu';".formatted(consultarId());
        GeradorLog.log(TagNiveisLog.INFO, "Consultando Hardware CPU...", Modulo.CAPTURA_HARDWARE);
        return con02.queryForObject(comandoSql, Integer.class);
    }

//    public Integer consultarHardwareRam() {
//        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
//                "where idMaquina = %d and tipoHardware = 'Ram';".formatted(consultarId());
//        return con.queryForObject(comandoSql, Integer.class);
//    }

    public Integer consultarHardwareRam() {
        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
                "where idMaquina = %d and tipoHardware = 'Ram';".formatted(consultarId());
        GeradorLog.log(TagNiveisLog.INFO, "Consultando Hardware RAM...", Modulo.CAPTURA_HARDWARE);
        return con02.queryForObject(comandoSql, Integer.class);
    }

//    public Integer consultarHardwareDisco() {
//        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
//                "where idMaquina = %d and tipoHardware = 'Disco';".formatted(consultarId());
//        return con.queryForObject(comandoSql, Integer.class);
//    }

    public Integer consultarHardwareDisco() {
        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
                "where idMaquina = %d and tipoHardware = 'Disco';".formatted(consultarId());
        GeradorLog.log(TagNiveisLog.INFO, "Consultando Hardware DISCO...", Modulo.CAPTURA_HARDWARE);
        return con02.queryForObject(comandoSql, Integer.class);
    }

//    public Integer consultarHardwareRede() {
//        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
//                "where idMaquina = %d and tipoHardware = 'Rede';".formatted(consultarId());
//        return con.queryForObject(comandoSql, Integer.class);
//    }

    public Integer consultarHardwareRede() {
        String comandoSql = "SELECT idHardware from InfoHardware join Maquina on idMaquina = fkMaquina " +
                "where idMaquina = %d and tipoHardware = 'Rede';".formatted(consultarId());
        GeradorLog.log(TagNiveisLog.INFO, "Consultando Hardware REDE...", Modulo.CAPTURA_HARDWARE);
        return con02.queryForObject(comandoSql, Integer.class);
    }

//    public String consultarUsuarioPorId() {
//        Integer ultimoIdMaquina = consultarId();
//        if (ultimoIdMaquina != null) {
//            String querySql = "SELECT usuario from Maquina where idMaquina = %d".formatted(ultimoIdMaquina);
//            String usuario = con.queryForObject(querySql, String.class);
//            return usuario;
//        }
//        System.out.println("Por favor insira uma máquina na parte de Dashboard no nosso site institucional");
//        System.exit(0);
//        return null;
//    }

    public String consultarUsuarioPorId() {
        Integer ultimoIdMaquina = consultarId();
        if (ultimoIdMaquina != null) {
            String querySql = "SELECT usuario from Maquina where idMaquina = %d".formatted(ultimoIdMaquina);
            usuario = con02.queryForObject(querySql, String.class);
            GeradorLog.log(TagNiveisLog.INFO, "Consultando usuário: %s por ID da Máquina".formatted(usuario), Modulo.CAPTURA_HARDWARE);
            return usuario;
        } else {
            System.out.println("Por favor insira uma máquina na parte de Dashboard no nosso site institucional");
            GeradorLog.log(TagNiveisLog.INFO, "Consulta de usuário por ID: 0", Modulo.CAPTURA_HARDWARE);
            System.exit(0);
            return null;
        }
    }

    public Looca getLooca() {
        return looca;
    }

    public void setLooca(Looca looca) {
        this.looca = looca;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Long getTempoAtividade() {
        return tempoAtividade;
    }

    public void setTempoAtividade(Long tempoAtividade) {
        this.tempoAtividade = tempoAtividade;
    }

    public Integer getFkProjeto() {
        return fkProjeto;
    }

    public void setFkProjeto(Integer fkProjeto) {
        this.fkProjeto = fkProjeto;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    @Override
    public String toString() {
        return """
                idMaquina: %d
                usuario: '%s'
                sistemaOperacional: '%s'
                temperatura: %.2f
                tempoAtividade: %d
                fkProjeto: %d
                fkEmpresa: %d""".formatted(idMaquina,
                usuario,
                sistemaOperacional,
                temperatura,
                tempoAtividade,
                fkProjeto,
                fkEmpresa);
    }
}
