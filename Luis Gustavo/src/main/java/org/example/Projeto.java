package org.example;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class Projeto {
    private Integer idProjeto;
    private String nomeDemanda;
    private Integer quantidadeDeMaquinas;
    private String dataInicio;
    private String dataTermino;
    private String descricao;
    private String responsavel;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();

    public Projeto(String nomeDemanda,
                   Integer quantidadeDeMaquinas,
                   String descricao,
                   String responsavel,
                   Integer fkEmpresa) {
        this.nomeDemanda = nomeDemanda;
        this.quantidadeDeMaquinas = quantidadeDeMaquinas;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.fkEmpresa = fkEmpresa;
    }
    public Projeto() {
    }

    public void inserirDadosProjeto(){
        con.update("INSERT INTO Projeto (nomeDemanda, quantidadeDeMaquinas, dataInicio, dataTermino," +
                "descricao, responsavel, fkEmpresa)" +
                        "values (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?)",
                nomeDemanda,
                quantidadeDeMaquinas,
                descricao,
                responsavel,
                fkEmpresa);
    }
    public Integer consultarId(){
        List<Integer> idsProjeto;

        String comandoSql = ("SELECT idEmpresa from Empresa");
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

    public Integer getQuantidadeDeMaquinas() {
        return quantidadeDeMaquinas;
    }

    public void setQuantidadeDeMaquinas(Integer quantidadeDeMaquinas) {
        this.quantidadeDeMaquinas = quantidadeDeMaquinas;
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
                Quantidade de máquinas: %d
                Data de início: %s
                Data de término: %s
                descricão: %s
                responsável: %s
                fkEmpresa: %d""".formatted(
                        idProjeto,
                nomeDemanda,
                quantidadeDeMaquinas,
                dataInicio,
                dataTermino,
                descricao,
                responsavel,
                fkEmpresa
        );
    }
}
