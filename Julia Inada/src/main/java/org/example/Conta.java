package org.example;

import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conta {
    private Integer idConta;
    private String login;
    private String senha;
    private String siglaConta;
    private String dataCriacao;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private ConexaoServer conexao02 = new ConexaoServer();
    private JdbcTemplate con = conexao.getConexaoBanco();
    // private JdbcTemplate con02 = conexao02.getConexaoBanco();


    public Conta(String login, String senha, String siglaConta, Integer fkEmpresa) {
        this.login = login;
        this.senha = senha;
        this.siglaConta = siglaConta;
        this.fkEmpresa = fkEmpresa;
    }

    public Conta() {
    }

    public void inserirDadosConta(){
        try {
            con.update("INSERT INTO Conta (login, senha, siglaConta, dataCriacao, fkEmpresa)" +
                            "values (?, ?, ?, CURRENT_TIMESTAMP, ?)",
                    login, senha, siglaConta, fkEmpresa);

            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data Local/MySQL DB: Table: %s".formatted(Tabelas.CONTA.getDescricaoTabela()), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values for login: %s / siglaConta: %s for ID.%s.redata: %d".formatted(login, siglaConta, Tabelas.CONTA.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);

//            con02.update("INSERT INTO Conta (login, senha, siglaConta, dataCriacao, fkEmpresa)" +
//                            "values (?, ?, ?, CURRENT_TIMESTAMP, ?)",
//                    login, senha, siglaConta, fkEmpresa);

//            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.CONTA.getDescricaoTabela()), Modulo.ENVIO_DADOS);

        }catch (RuntimeException e){
            System.out.println("erro de conexão 'Conta' sql " + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL: %s".formatted(Tabelas.CONTA.getDescricaoTabela()), Modulo.ALERTA);
        }
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
