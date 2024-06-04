package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Maquina {
    Looca looca = new Looca();
    private Integer idMaquina;
    private String usuario;
    private String sistemaOperacional;
    private Double temperatura;
    private Long tempoAtividade;
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
        GeradorLog.log(TagNiveisLog.INFO, "Iniciando captura de dados da máquina: %d...".formatted(idMaquina), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "User: %s".formatted(usuario), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "OS: %s".formatted(sistemaOperacional), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Temperature: %.2f".formatted(temperatura), Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Uptime: " + tempoAtividade + "hours", Modulo.CAPTURA_HARDWARE);
        GeradorLog.log(TagNiveisLog.INFO, "Process finished..", Modulo.CAPTURA_HARDWARE);
    }

    public void inserirDadosMaquina() {
        try {
            con.update("INSERT INTO Maquina (usuario, sistemaOperacional, temperatura, tempoAtividade, " +
                            "fkProjeto, fkEmpresa) values (?, ?, ?, ?, ?, ?)", usuario, sistemaOperacional,
                    temperatura, tempoAtividade, fkProjeto, fkEmpresa);

            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data Local/MySQL DB: Table: %s".formatted(Tabelas.MAQUINA.getDescricaoTabela()), Modulo.ENVIO_DADOS);

//            con02.update("INSERT INTO Maquina (usuario, sistemaOperacional, temperatura, tempoAtividade, " +
//                            "fkProjeto, fkEmpresa) values (?, ?, ?, ?, ?, ?)", usuario, sistemaOperacional,
//                    temperatura, tempoAtividade, fkProjeto, fkEmpresa);

//            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.MAQUINA.getDescricaoTabela()), Modulo.ENVIO_DADOS);

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Maquina' sql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL: %s".formatted(Tabelas.MAQUINA.getDescricaoTabela()), Modulo.ALERTA);
        }
    }

    public Integer consultarId() {
        List<Integer> idsMaquina;

        String comandoSql = ("SELECT idMaquina from Maquina");
        idsMaquina = con.queryForList(comandoSql, Integer.class);
        return idsMaquina.get(idsMaquina.size() - 1);
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
