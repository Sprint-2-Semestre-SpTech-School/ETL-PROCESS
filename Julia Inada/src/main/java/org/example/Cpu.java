package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import org.h2.util.json.JsonConstructorUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class Cpu {
    Looca looca = new Looca();
    Processador processador = looca.getProcessador();
    Memoria memoria = looca.getMemoria();
    DiscoGrupo disco = looca.getGrupoDeDiscos();
    private String fabricante;
    public String obterFabricante(){
        Processador processador = looca.getProcessador();
        processador.getFabricante();
        fabricante = processador.getFabricante();
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoBanco();
        String CNPJ = "00000000";
        System.out.println(fabricante);
        con.update("INSERT INTO Empresa (nomeEmpresa, CNPJ) values (?, ?)", fabricante, CNPJ);
        return fabricante;
    }

    public Long processoExtracao() {
        Memoria memoria1 = looca.getMemoria();
        Long usoMemoria = memoria1.getEmUso();

        return usoMemoria;
    }

    public Double processoTransformacao() {
        Processador processador1 = looca.getProcessador();
        Double usoCpu = processador1.getUso();
        Long frequenciaCpu = processador1.getFrequencia();

        return usoCpu;
    }

    public Integer processoCarregamento() {
        DiscoGrupo disco1 = looca.getGrupoDeDiscos();
        Integer qtdVolumes = disco1.getQuantidadeDeVolumes();

        return qtdVolumes;
    }





}
