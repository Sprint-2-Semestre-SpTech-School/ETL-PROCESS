package org.example;

import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class Projeto {
    private Integer idProjeto;
    private String nomeDemanda;
    private String dataInicio;
    private String dataTermino;
    private String descricao;
    private String responsavel;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private ConexaoServer conexao02 = new ConexaoServer();


    public Projeto(String nomeDemanda,
                   String descricao,
                   String responsavel,
                   Integer fkEmpresa) {
        this.nomeDemanda = nomeDemanda;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.fkEmpresa = fkEmpresa;
    }

    public Projeto() {
    }

    public void inserirDadosProjeto(){
        try {
            JdbcTemplate con = conexao.getConexaoBanco();
            JdbcTemplate con02 = conexao02.getConexaoBanco();

            con.update("INSERT INTO Projeto (nomeDemanda, dataInicio, dataTermino," +
                            "descricao, responsavel, fkEmpresa)" +
                            "values (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?)",
                    nomeDemanda, descricao, responsavel, fkEmpresa);

            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data Local/MySQL DB: Table: %s".formatted(Tabelas.PROJETO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - nomeDemanda: %s for ID.%s.redata: %d".formatted(nomeDemanda, Tabelas.PROJETO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - dataInicio: %s for ID.%s.redata: %d".formatted(dataInicio, Tabelas.PROJETO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - dataTermino: %s for ID.%s.redata: %d".formatted(dataTermino, Tabelas.PROJETO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - descricao: %s for ID.%s.redata: %d".formatted(descricao, Tabelas.PROJETO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - responsavel: %s for ID.%s.redata: %d".formatted(responsavel, Tabelas.PROJETO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);

//            con02.update("INSERT INTO Projeto (nomeDemanda, dataInicio, dataTermino," +
//                            "descricao, responsavel, fkEmpresa)" +
//                            "values (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?)",
//                    nomeDemanda, descricao, responsavel, fkEmpresa);

//            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.PROJETO.getDescricaoTabela()), Modulo.ENVIO_DADOS);

        }catch (RuntimeException e){
            System.out.println("Erro de conexão 'Maquina' sql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL: %s".formatted(Tabelas.PROJETO.getDescricaoTabela()), Modulo.ALERTA);
        }
    }

    public Integer consultarId(){
        JdbcTemplate con = conexao.getConexaoBanco();
        List<Integer> idsProjeto;

        String comandoSql = ("SELECT idProjeto from Projeto");
        idsProjeto = con.queryForList(comandoSql, Integer.class);
        return idsProjeto.get(idsProjeto.size() - 1);
    }

    public Integer getIdProjeto() {
        return idProjeto;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public String getNomeDemanda() {
        return nomeDemanda;
    }

    public void setNomeDemanda(String nomeDemanda) {
        this.nomeDemanda = nomeDemanda;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(String dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public String toString() {
        return """
                idProjeto: %d
                Nome da demanda: %s
                Data de início: %s
                Data de término: %s
                descricão: %s
                responsável: %s
                fkEmpresa: %d""".formatted(
                        idProjeto,
                nomeDemanda,
                dataInicio,
                dataTermino,
                descricao,
                responsavel,
                fkEmpresa
        );
    }
}
