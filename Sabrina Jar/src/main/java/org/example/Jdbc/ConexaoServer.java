package org.example.Jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoServer{
    public JdbcTemplate conexaoBanco;

    public ConexaoServer() {
        BasicDataSource dataSource = new BasicDataSource();
         dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         dataSource.setUrl("jdbc:sqlserver://44.194.59.3:1433;databaseName=redata;encrypt=true;trustServerCertificate=true");

        dataSource.setUsername("sa");
        dataSource.setPassword("redata");

         conexaoBanco = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConexaoBanco() {
        return conexaoBanco;
    }
}
