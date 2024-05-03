package org.example;

import com.github.britooo.looca.api.group.dispositivos.DispositivosUsbGrupo;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
    public static void main(String[] args) {
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
                5,
                "Aumentar vendas de Coca-Cola na Zona Norte",
                "Julia", fkEmpresa);
        projetoDemo.inserirDadosProjeto();
        Integer fkProjeto = projetoDemo.consultarId();
        System.out.println(projetoDemo);

        Maquina maquinaDemo = new Maquina(fkEmpresa, fkProjeto);
        maquinaDemo.capturarDadosMaquina();
        maquinaDemo.inserirDadosMaquina();
        Integer fkMaquina = maquinaDemo.consultarId();
        System.out.println(maquinaDemo);

        infoHardware infoHardwareDemo = new infoHardware(fkMaquina);
        infoHardwareDemo.capturarDadosInfoHardware();
        Integer fkCodHardware = infoHardwareDemo.consultarId();

        Cpu cpuDemo = new Cpu(fkCodHardware, fkMaquina);
        cpuDemo.capturarDadosCpu();
        System.out.println(cpuDemo);

        Ram ramDemo = new Ram(fkCodHardware, fkMaquina);
        ramDemo.capturarDadosRam();
        System.out.println(ramDemo);

        Rede redeDemo = new Rede(fkCodHardware, fkMaquina);
        redeDemo.capturarDadosRede();
        System.out.println(redeDemo);

        Disco discoDemo = new Disco(fkCodHardware, fkMaquina);
        discoDemo.capturarDadosDisco();
        Integer fkDisco = discoDemo.getIdDisco();
        System.out.println(discoDemo);

        Volume volumeDemo = new Volume(fkDisco, fkCodHardware, fkMaquina);
        volumeDemo.capturarDadosVolume();
        System.out.println(volumeDemo);

        dispositivosUSB dispositivosUSBDemo = new dispositivosUSB();
        dispositivosUSBDemo.listarUSB();
        System.out.println(dispositivosUSBDemo);
    }
}
