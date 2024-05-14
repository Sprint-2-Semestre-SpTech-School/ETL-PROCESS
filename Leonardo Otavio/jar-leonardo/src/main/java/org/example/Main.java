package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.servicos.ServicoGrupo;
import com.github.britooo.looca.api.group.sistema.Sistema;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
    public static void main(String[] args) {

        Looca looca = new Looca();

        // Processos de analise
//        Sistema sistema = looca.getSistema();
//        System.out.println(sistema.getSistemaOperacional());
//
//        Memoria memoria = looca.getMemoria();
//        System.out.println("\n" + memoria.getTotal());
//
//        Processador processador = looca.getProcessador();
//        System.out.println("\n" + processador.getId());
//
//        DiscoGrupo discoGrupo = looca.getGrupoDeDiscos();
//        System.out.println("\n" + discoGrupo.getQuantidadeDeDiscos());
//
//        ServicoGrupo servicoGrupo = looca.getGrupoDeServicos();
//        System.out.println("\n" + servicoGrupo.getServicosInativos());
//
//        ProcessoGrupo processoGrupo = looca.getGrupoDeProcessos();
//        System.out.println("\n" + processoGrupo.getTotalProcessos());


        // Login
//        Funcionario func01 = new Funcionario();
//
//        func01.fazerLogin();

        Gestor gest01 = new Gestor();

        gest01.fazerLogin();

    }
}