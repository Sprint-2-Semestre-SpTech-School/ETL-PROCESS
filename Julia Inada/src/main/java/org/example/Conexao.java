package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    public JdbcTemplate conexaoBanco;

    Looca looca = new Looca();

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/redata");

        if (looca.getSistema().getSistemaOperacional().equalsIgnoreCase("windows")) {
            dataSource.setUsername("root");
            dataSource.setPassword("ADS70#sptech");
            conexaoBanco = new JdbcTemplate(dataSource);
        } else {
            // A conexão do MySql deve ser essa
            dataSource.setUsername("root");
            dataSource.setPassword("ADS70#sptech");
            conexaoBanco = new JdbcTemplate(dataSource);
        }
    }

    public JdbcTemplate getConexaoBanco() {
        return conexaoBanco;
    }
}


