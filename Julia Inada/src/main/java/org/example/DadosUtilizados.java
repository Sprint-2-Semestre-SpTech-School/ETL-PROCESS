package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;
import oshi.hardware.NetworkIF;

public class DadosUtilizados {
    public static void main(String[] args) {

        Looca looca = new Looca();
        Processador cpu01 = looca.getProcessador();
        Memoria memoria01 = looca.getMemoria();
        DiscoGrupo discoGrupo = looca.getGrupoDeDiscos();
        Disco disco01 = looca.getGrupoDeDiscos().getDiscos().get(0);
        Volume volume01 = looca.getGrupoDeDiscos().getVolumes().get(0);
        Rede rede01 = looca.getRede();
        RedeParametros redeParam01 = looca.getRede().getParametros();
        RedeInterfaceGroup redeGroup = looca.getRede().getGrupoDeInterfaces();
        RedeInterface redeInterface = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(2);
        // ====================================== DADOS GERAIS ============================================//
        System.out.println("\n");
        System.out.println("Dados que eu não sei");
        System.out.println("Sistema Operacional " + looca.getSistema().getSistemaOperacional());
        System.out.println("Tempo de atividade " + looca.getSistema().getTempoDeAtividade() / 3600 + "h"); // Temp atv em sec convertido pra horas
        System.out.println("Temperatura maquina" + looca.getTemperatura());
        System.out.println("Inicializado " + looca.getSistema().getInicializado());


        // ========================================DADOS DE CPU====================================//
        System.out.println("DADOS DE CPU");
        System.out.println("Id de CPU: " + cpu01.getId()); // Id da própria tabela
        // System.out.println(cpu01.getFabricante()); O nome já vem junto com outros
        System.out.println("Frequência: " + cpu01.getFrequencia() / 100000 + "Mhz"); // Convertido para Mhz
        System.out.println("Nome da CPU: " + cpu01.getNome()); // Usar para pegar o nome e especificar, separar talvez?
        System.out.println("Num de CPU físicas: " + cpu01.getNumeroCpusFisicas()); // Mais cpu's físicas + velocidade Reecaminha 5 //
        System.out.println("Num de CPU lógicas: " + cpu01.getNumeroCpusLogicas());
        System.out.println("Pacotes físicos: " + cpu01.getNumeroPacotesFisicos());
        System.out.println("Uso de CPU: " + cpu01.getUso() + "%"); // Uso de cpu, precisa arrumar
        // System.out.println(cpu01.getIdentificador());
        // System.out.println(cpu01.getMicroarquitetura());
        System.out.println("\n");

        // ======================================== Dados DE MEMÓRIA =====================================//
        System.out.println("DADOS DE MEMÓRIA");
        System.out.println("Memória disponível: " + memoria01.getDisponivel() / (1024 * 1024) + "Mb");
        System.out.println("Memória em uso: " + memoria01.getEmUso() / 1e9);
        System.out.println("Memória total: " + memoria01.getTotal() / (1024 * 1024) + "Mb");
        // O que iremos fazer com isso? Colocar eles em % em relação um ao outro se baseando na aula de KPI.
        // Ex: (Dado meio vaidade) Sua empresa ainda tem x de pool de ram para aplicar em geral.
        // Ex: X% da sua ram total está em uso.

        System.out.println("\n");
        // ========================================= DADOS DE DISCO ========================================//
        System.out.println("DADOS DE DISCO");
        System.out.println("Nome disco: " + disco01.getNome());
        System.out.println("Escritas: " + disco01.getEscritas() + " Vezes");
        System.out.println("Bytes de escritas: " + disco01.getBytesDeEscritas() / (1024 * 1024) + "Mb"); // Mais útil para o volume total gravado no disco
        System.out.println("Leituras: " + disco01.getLeituras() + " Vezes");
        System.out.println("Bytes de leitura: " + disco01.getBytesDeLeitura() / (1024 * 1024) + "Mb");
        System.out.println("Qtd de discos: " + discoGrupo.getQuantidadeDeDiscos()); // Importante para separar depois
        System.out.println("Tamanho total " + discoGrupo.getTamanhoTotal());
        System.out.println("Tempo de transferência: " + disco01.getTempoDeTransferencia() / 1000 + "s");
        System.out.println("\n");

        System.out.println("Informações do volume do disco");
        System.out.println("Ponto de montagem: " + volume01.getPontoDeMontagem());
        System.out.println("Sistema armazenamento: " + volume01.getTipo());
        System.out.println("Volume disponível: " + volume01.getDisponivel() / 1000000 + "Mb");
        System.out.println("Volume total: " + volume01.getTotal() / 1000000 + "Mb");
        System.out.println("Quantidade de volume: " + discoGrupo.getQuantidadeDeVolumes());


        // Não achei os dois métodos de volume úteis
        System.out.println("\n");
        // ========================================= DADOS DE REDE =========================================//
        System.out.println("DADOS DE REDE ");
        System.out.println("Endereço ipv4 " + redeInterface.getEnderecoIpv4()); // Saber qual o ip que os computadores estão
        // Conectados
        // System.out.println("Endereço ipv6 " + redeInterface.getEnderecoIpv6()); // Duvida, precisa do ipv4 mesmo?
        System.out.println("Endereço MAC " + redeInterface.getEnderecoMac()); // Endereço de ID da rede
        System.out.println("Bytes recebidos " + redeInterface.getBytesRecebidos() / (1024 * 1024) + "Mb"); // Se os computadores estão recebendo bem a rede
        System.out.println("Nome exibicação " + redeInterface.getNomeExibicao()); // Nome da rede que eles estão conectados
        System.out.println("Pacotes enviados " + redeInterface.getPacotesEnviados()); // Mais dados de pacote
        System.out.println("Pacotes recebidos " + redeInterface.getPacotesRecebidos()); // Mais dados de pacote
        System.out.println("Interfaces: " + looca.getRede().getGrupoDeInterfaces());
        // System.out.println("Rede DNS " + redeParam01.getServidoresDns());
        System.out.println("Nome do host " + redeParam01.getHostName());
        // System.out.println(redeInterface);
        // ======================================= Coisas que não sei =====================================//
        System.out.println(looca.getDispositivosUsbGrupo().getDispositivosUsbConectados()); // Só vê unidade de disco
        System.out.println("\n\n");
    }
}
