package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.logging.GeradorLog;
import org.example.logging.Modulo;
import org.example.logging.Tabelas;
import org.example.logging.TagNiveisLog;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Looca looca = new Looca();
        Locale defaultLocale = Locale.getDefault();

        GeradorLog.log(TagNiveisLog.INFO, "Language: " + defaultLocale.getDisplayLanguage(), Modulo.GERAL);
        GeradorLog.log(TagNiveisLog.INFO, "OS name: " + looca.getSistema().getSistemaOperacional(), Modulo.GERAL);
        GeradorLog.log(TagNiveisLog.INFO, "Arch: " + looca.getSistema().getArquitetura() + "x bits", Modulo.GERAL);
        GeradorLog.log(TagNiveisLog.INFO, "Iniciando a aplicação...", Modulo.GERAL);

        Empresa empresaDemo = new Empresa("ReData.INC",
                "53719031000163",
                "03325764",
                "São Paulo",
                "Rua Database",
                "777",
                "DataLake",
                "Camada de Load");
        empresaDemo.inserirDadosEmpresa(); // Inserindo dados no MySql

        System.out.println(empresaDemo.consultarId()); // Consultar o Id Atual
        Integer fkEmpresa = empresaDemo.consultarId(); // Definindo o Id para as futuras operações

        System.out.println(empresaDemo);
        System.out.println("\n");

        Contato contatoDemo = new Contato("James Heat Field",
                "James@gmail.com",
                "11974216702",
                fkEmpresa);
        contatoDemo.inserirDadosContato();

        Conta contaDemo = new Conta("ReData2024", "ReData100", "ADM", fkEmpresa);
        contaDemo.inserirDadosConta();
        System.out.println(contaDemo);

        System.out.println(contatoDemo);
        System.out.println("\n");

        Projeto projetoDemo = new Projeto("Venda de Coca-Cola",
                "Aumentar vendas de Coca-Cola na Zona Norte",
                "Julia", fkEmpresa);
        projetoDemo.inserirDadosProjeto();
        Integer fkProjeto = projetoDemo.consultarId();
        System.out.println(projetoDemo);

        Maquina maquinaDemo = new Maquina(fkProjeto, fkEmpresa);
        maquinaDemo.capturarDadosMaquina();
        maquinaDemo.inserirDadosMaquina();
        Integer fkMaquina = maquinaDemo.consultarId();
        System.out.println("fkMaquina: " + fkMaquina);
        System.out.println(maquinaDemo);

        Cpu cpu = new Cpu();
//        cpu.capturarDados();
        cpu.inserirDados();

        Ram ram = new Ram();
//        ram.capturarDados();
        ram.inserirDados();

        Disco disco = new Disco();
//        disco.capturarDados();
        disco.inserirDados();

       Rede rede = new Rede();
//       rede.capturarDados();
       rede.inserirDados();

        JarJulia teste = new JarJulia();
        teste.escolherProcesso();


        // METODO PARA INSTANCIA, CRIAR E PLOTAR O ALERTA NO SLACK:
//        if(temperatura > metrica) {
        try {
            JSONObject json = new JSONObject();
            json.put("text", "Aqui colocaremos os alertas!!");
            Slack.sendMessage(json);
        } catch (IOException e) {
            System.out.println("Deu ruim no slack" + e);
        }
//    } É SOBRE ISSO

//        cpu.inserirDados();
//        rede.inserirDados();
//        disco.inserirDados();

    }
}
