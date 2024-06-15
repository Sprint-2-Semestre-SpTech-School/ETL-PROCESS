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
            ConexaoServer conexaoLogin = new ConexaoServer();
            JdbcTemplate conLogin = conexaoLogin.getConexaoBanco();

//            ================================================ LOCAL ======================================
//            try {
//                    Integer checkEmpresaTable = conLogin.queryForObject("SELECT 1 FROM Empresa LIMIT 1", Integer.class);
//                if (checkEmpresaTable == null) {
//                    System.out.println("A tabela Empresa não existe ou está vazia. Adicione uma empresa no site.");
//                    System.exit(0);
//                }
//            } catch (Exception e) {
//                System.out.println("A tabela Empresa não existe ou retornou um valor inválido: " + e.getMessage());
//                System.exit(0);
//            }
//
//            try {
//                conLogin.update("INSERT IGNORE INTO Conta (login, senha, siglaConta, dataCriacao ,fkEmpresa) VALUES ((select nomeEmpresa from Empresa where idEmpresa = (select max(idEmpresa) from Empresa)), 'SPtechPI', 'FCM', current_timestamp, 1)");
//            } catch (Exception e) {
//                System.out.println("Erro ao inserir na tabela Conta: " + e.getMessage());
//                return;
//            }
//
//            try {
//                Integer checkTable = conLogin.queryForObject("SELECT 1 FROM Conta LIMIT 1", Integer.class);
//                if (checkTable == null) {
//                    System.out.println("A tabela Conta não existe ou retornou um valor inválido");
//                    return;
//                }
//            } catch (Exception e) {
//                System.out.println("A tabela Conta não existe ou retornou um valor inválido: " + e.getMessage());
//                System.exit(0);
//            }


            List<Login> loginDoBanco = conLogin.query("SELECT * FROM Conta",
                    new BeanPropertyRowMapper<>(Login.class));

            boolean usuarioEncontrado = false;

            while (!usuarioEncontrado) {
                System.out.println("Insira o nome de usuário:");
                String usuarioInserido = inputLine.nextLine();

                Integer indiceLogin = null;

                for (Integer i = 0; i < loginDoBanco.size(); i++) {
                    Login loginDaVez = loginDoBanco.get(i);
                    if (loginDaVez.getLogin().equals(usuarioInserido)) {
                        System.out.println("Usuário encontrado");
                        indiceLogin = i;
                        usuarioEncontrado = true;
                        break;
                    }
                }

                if (!usuarioEncontrado) {
                    System.out.println("Usuário não encontrado, tente novamente");
                } else {
                    System.out.println("Insira aqui sua senha:");
                    String senhaInserida = inputLine.nextLine();

                    while (true) {
                        Login senhaDaVez = loginDoBanco.get(indiceLogin);
                        if (senhaDaVez.getSenha().equals(senhaInserida)) {
                            System.out.println("Senha verificada com sucesso");
                            return;
                        } else {
                            System.out.println("Senha incorreta, tente novamente \n");
                            System.out.println("Insira aqui sua senha:");
                            senhaInserida = inputLine.nextLine();
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
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
                Obrigado, seus dados foram verificados.
                Agora seus dados estão sendo capturados.""".formatted(login);
    }
}
