package org.example;

import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Login {
    private Integer idConta;
    protected String login;
    protected String senha;
    private String siglaConta;
    private Date dataCriacao;
    private Integer fkEmpresa;


    public Login(Integer idConta,
                 String login,
                 String senha,
                 String siglaConta,
                 Date dataCriacao,
                 Integer fkEmpresa) {
        this.idConta = idConta;
        this.login = login;
        this.senha = senha;
        this.siglaConta = siglaConta;
        this.dataCriacao = dataCriacao;
        this.fkEmpresa = fkEmpresa;
    }

    public Login() {
    }

    private Scanner inputLine = new Scanner(System.in);

    public void validacaoLogin() {

        try {
//            ConexaoServer conexaoLogin = new ConexaoServer();
            Conexao conexaoLogin = new Conexao();
            JdbcTemplate conLogin = conexaoLogin.getConexaoBanco();

            conLogin.update(" INSERT INTO Conta (login, senha, siglaConta, dataCriacao ,fkEmpresa) VALUES ((select nomeEmpresa from Empresa where idEmpresa = (select max(idEmpresa) from Empresa)), 'SPtechPI', 'FCM', current_timestamp, 1)");

            List<Login> loginDoBanco = conLogin.query("SELECT * FROM Conta",
                    new BeanPropertyRowMapper<>(Login.class));

            List<String> loginUsuario = conLogin.queryForList("SELECT login FROM Conta", String.class);

            List<String> senhaUsuario = conLogin.queryForList("SELECT senha FROM Conta", String.class);

//      List<Metodos> loginTeste = conLogin.query("SELECT idConta as id, login, senha FROM Conta WHERE login = ?  AND senha = ?",
//                new BeanPropertyRowMapper<>(Metodos.class),login,senha);

//      MÉTODO
            System.out.println("Insira o nome de usuário:");
            String usuarioInserido = inputLine.nextLine();

            Integer indiceLogin = 0;

            for (Integer i = 0; i < loginDoBanco.size(); i++) {
                Login loginDaVez = loginDoBanco.get(i);
                if (loginDaVez.getLogin().equals(usuarioInserido)) {
                    System.out.println("Usuário encontrado");
                    indiceLogin = i;
                    login = usuarioInserido;
                    break;
                } else {

                    if (i.equals(loginDoBanco.size() - 1)) {

                        System.out.println("Usuário não encontrado, tente novamente");
                        System.out.println("Insira aqui seu nome de usuário:");
                        usuarioInserido = inputLine.nextLine();

                        i = -1;
                    }
                }
            }

            System.out.println("Insira aqui sua senha:");
            String senhaInserida = inputLine.nextLine();

            for (Integer i = 0; i < loginDoBanco.size(); i++) {
                Login senhaDaVez = loginDoBanco.get(i);
                if (senhaDaVez.getSenha().equals(senhaInserida) && indiceLogin.equals(i)) {
                    System.out.println("Senha verificada com sucesso");
                    break;
                } else {
                    if (i.equals(loginDoBanco.size() - 1)) {

                        System.out.println("Senha incorreta, tente novamente \n");
                        System.out.println("Insira aqui sua senha:");
                        senhaInserida = inputLine.nextLine();
                        i = -1;
                    }
                }
            }
        }catch (RuntimeException e){
            System.out.println("Erro no login" + e.getMessage());
        }
    }

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
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
                Obrigada, %s, por fazer login no nosso sistema.
                Você será direcionado à sua página de login.""".formatted(login);
    }
}
