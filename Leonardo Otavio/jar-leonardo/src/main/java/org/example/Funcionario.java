package org.example;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Scanner;

public class Funcionario {

    private String nome;
    private String area;
    private Boolean temConta;
    private Integer idConta;
    private String email;
    private String senha;
    private String siglaConta;
    private String dataCriacao;
    private Integer fkEmpresa;

    private Scanner inputLine = new Scanner(System.in);

    public Funcionario(String nome,
                       String area,
                       Boolean temConta,
                       Integer idConta,
                       String email,
                       String senha,
                       String siglaConta,
                       String dataCriacao,
                       Integer fkEmpresa) {
        this.nome = nome;
        this.area = area;
        this.temConta = temConta;
        this.idConta = idConta;
        this.email = email;
        this.senha = senha;
        this.siglaConta = siglaConta;
        this.dataCriacao = dataCriacao;
        this.fkEmpresa = fkEmpresa;
    }

    public Funcionario() {

    }

    public void fazerLogin() {

        Conexao conexaoLogin = new Conexao();
        JdbcTemplate conLogin = conexaoLogin.getConexaoDoBanco();

        List<Funcionario> loginDoBanco = conLogin.query("SELECT * FROM Conta",
                new BeanPropertyRowMapper<>(Funcionario.class));

        System.out.println("Quer acessar sua página de usuário e acompanhar o monitoramento?");

        System.out.println("Insira aqui seu e-mail:");
        String usuarioInserido = inputLine.nextLine();

        Integer indiceLogin = 0;

        for (Integer i = 0; i < loginDoBanco.size(); i ++) {
            Funcionario loginDaVez = loginDoBanco.get(i);
            if (loginDaVez.getEmail().equals(usuarioInserido)) {
                System.out.println("Usuário encontrado");
                indiceLogin = i;
                email = usuarioInserido;
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

        for (Integer i = 0; i < loginDoBanco.size(); i ++) {
            Funcionario senhaDaVez = loginDoBanco.get(i);
            if (senhaDaVez.getSenha().equals(senhaInserida) && indiceLogin.equals(i)) {
                System.out.println("""
                        Senha verificada com sucesso.
                        
                        Obrigado, %s, por fazer login no nosso sistema.
                        O usuário será redirecionado para a página inicial.
                        """.formatted(email));
                break;
            } else {
                if (i.equals(loginDoBanco.size() - 1)) {

                    System.out.println("Senha incorreta, tente novamente");
                    System.out.println("Insira aqui sua senha:");
                    senhaInserida = inputLine.nextLine();

                    i = -1;
                }
            }
        }

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Boolean getTemConta() {
        return temConta;
    }

    public void setTemConta(Boolean temConta) {
        this.temConta = temConta;
    }

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", area='" + area + '\'' +
                ", temConta=" + temConta +
                ", idConta=" + idConta +
                ", login='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", siglaConta='" + siglaConta + '\'' +
                ", dataCriacao='" + dataCriacao + '\'' +
                ", fkEmpresa=" + fkEmpresa +
                '}';
    }
}