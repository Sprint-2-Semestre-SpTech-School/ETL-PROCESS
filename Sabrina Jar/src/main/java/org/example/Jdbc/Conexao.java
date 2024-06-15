package org.example.Jdbc;

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
            dataSource.setUsername("Re;Data");
            dataSource.setPassword("UrubuAbutre100");
            conexaoBanco = new JdbcTemplate(dataSource);
        } else {
            dataSource.setUsername("Re;Data");
            dataSource.setPassword("UrubuAbutre100");
            conexaoBanco = new JdbcTemplate(dataSource);
        }
    }

    public JdbcTemplate getConexaoBanco() {
        return conexaoBanco;
    }
}


