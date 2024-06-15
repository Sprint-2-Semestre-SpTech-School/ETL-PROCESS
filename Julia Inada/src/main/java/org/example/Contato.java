package org.example;

import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.springframework.jdbc.core.JdbcTemplate;

public class Contato {
    private Integer idContato;
    private String nome;
    private String email;
    private String telefone;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private ConexaoServer conexao02 = new ConexaoServer();
    private JdbcTemplate con = conexao.getConexaoBanco();
    // private JdbcTemplate con02 = conexao02.getConexaoBanco();

    public Contato(String nome, String email, String telefone, Integer fkEmpresa) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.fkEmpresa = fkEmpresa;
    }

    public Contato() {
    }

    public void inserirDadosContato(){
        try{
            con.update("INSERT INTO Contato (nome, email, telefone, fkEmpresa) values (?, ?, ?, ?)",
                    nome, email, telefone, fkEmpresa);

            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data Local/MySQL DB: Table: %s".formatted(Tabelas.CONTATO.getDescricaoTabela()), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - nome: %s for ID.%s.redata: %d".formatted(nome, Tabelas.CONTATO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - email: %s for ID.%s.redata: %d".formatted(email, Tabelas.CONTATO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);
            GeradorLog.log(TagNiveisLog.INFO, "SQL column new values - telefone: %s for ID.%s.redata: %d".formatted(telefone, Tabelas.CONTATO.getDescricaoTabela(), fkEmpresa), Modulo.ENVIO_DADOS);

//            con02.update("INSERT INTO Contato (nome, email, telefone, fkEmpresa) values (?, ?, ?, ?)",
//                    nome, email, telefone, fkEmpresa);

//            GeradorLog.log(TagNiveisLog.INFO, "Dados inseridos com sucesso! Re;Data SQL Server DB: Table: %s".formatted(Tabelas.CONTATO.getDescricaoTabela()), Modulo.ENVIO_DADOS);

        }catch (RuntimeException e){
            System.out.println("erro de conexão 'Contato' mysql" + e.getMessage());
            GeradorLog.log(TagNiveisLog.ERROR, "Erro de conexão SQL: %s".formatted(Tabelas.CONTATO.getDescricaoTabela()), Modulo.ALERTA);
        }
    }
    public Integer getIdContato() {
        return idContato;
    }

    public Conexao getConexao() {
        return conexao;
    }

    public JdbcTemplate getCon() {
        return con;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return """
                idContato: %d
                Nome: %s
                Telefone: %s
                fkEmpresa: %d""".formatted(idContato, nome, telefone, fkEmpresa);
    }
}


