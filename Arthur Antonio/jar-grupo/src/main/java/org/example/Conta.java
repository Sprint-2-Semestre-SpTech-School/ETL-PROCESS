package org.example;

import org.springframework.jdbc.core.JdbcTemplate;

public class Conta {
    private Integer idConta;
    private String login;
    private String senha;
    private String siglaConta;
    private String dataCriacao;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();
    public Conta(String login, String senha, String siglaConta, Integer fkEmpresa) {
        this.login = login;
        this.senha = senha;
        this.siglaConta = siglaConta;
        this.fkEmpresa = fkEmpresa;
    }
    public Conta() {
    }
    public void inserirDadosConta(){
        con.update("INSERT INTO Conta (login, senha, siglaConta, dataCriacao, fkEmpresa)" +
                        "values (?, ?, ?, CURRENT_TIMESTAMP, ?)",
                login, senha, siglaConta, fkEmpresa);
    }
    public Integer getIdConta() {
        return idConta;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSiglaConta() {
        return siglaConta;
    }

    public void setSiglaConta(String siglaConta) {
        this.siglaConta = siglaConta;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return """
                idConta: %d
                login: %s
                senha: %s
                siglaConta: %s
                dataCriacao: %s
                fkEmpresa: %s
                """.formatted(idConta,
                login,
                senha,
                siglaConta,
                fkEmpresa,
                dataCriacao);
    }
}
