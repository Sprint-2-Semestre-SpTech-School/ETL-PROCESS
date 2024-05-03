package org.example;

import com.github.britooo.looca.api.core.Looca;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class overloadTest {
    static Looca looca = new Looca();
    private static Maquina maquina = new Maquina(); // Static torna ela ligada a classe, portanto global

    public static void main(String[] args) throws IOException {
        String user = System.getProperty("user.name"); // Pegando usuário do sistema
        String caminho;
        //if (maquina.getSistemaOperacional().equalsIgnoreCase("windows")) {
            caminho = ("C:\\Users\\" + user + "\\Documents"); // Caminho onde a pasta será criada no seu PC
       // } else {
            caminho = System.getProperty("user.home");
       // }
            String nomeArquivo = "overchargeTest"; // Nome da pasta que será criada
            String caminhoPasta = caminho + File.separator + nomeArquivo; // O caminho da pasta para criá-la

            File dir = new File(caminhoPasta); // A classe File é genérica, serve para criar um arquivo
            dir.mkdir(); // Nesse caso mkdir, um diretório está sendo criado

            try { // Bloco de execução para lançar uma excecção
                System.out.println(" ________  ___      ___ _______   ________  ___       ________  ________  ________          _________  _______   ________  _________        ________  _______   ________  ________  _________  ________          \n" +
                        "|\\   __  \\|\\  \\    /  /|\\  ___ \\ |\\   __  \\|\\  \\     |\\   __  \\|\\   __  \\|\\   ___ \\        |\\___   ___|\\  ___ \\ |\\   ____\\|\\___   ___\\     |\\   __  \\|\\  ___ \\ |\\   ___ \\|\\   __  \\|\\___   ___|\\   __  \\  ___    \n" +
                        "\\ \\  \\|\\  \\ \\  \\  /  / \\ \\   __/|\\ \\  \\|\\  \\ \\  \\    \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\_|\\ \\       \\|___ \\  \\_\\ \\   __/|\\ \\  \\___|\\|___ \\  \\_|     \\ \\  \\|\\  \\ \\   __/|\\ \\  \\_|\\ \\ \\  \\|\\  \\|___ \\  \\_\\ \\  \\|\\  \\|\\__\\   \n" +
                        " \\ \\  \\\\\\  \\ \\  \\/  / / \\ \\  \\_|/_\\ \\   _  _\\ \\  \\    \\ \\  \\\\\\  \\ \\   __  \\ \\  \\ \\\\ \\           \\ \\  \\ \\ \\  \\_|/_\\ \\_____  \\   \\ \\  \\       \\ \\   _  _\\ \\  \\_|/_\\ \\  \\ \\\\ \\ \\   __  \\   \\ \\  \\ \\ \\   __  \\|__|   \n" +
                        "  \\ \\  \\\\\\  \\ \\    / /   \\ \\  \\_|\\ \\ \\  \\\\  \\\\ \\  \\____\\ \\  \\\\\\  \\ \\  \\ \\  \\ \\  \\_\\\\ \\           \\ \\  \\ \\ \\  \\_|\\ \\|____|\\  \\   \\ \\  \\       \\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\_\\\\ \\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\ \\  \\  ___ \n" +
                        "   \\ \\_______\\ \\__/ /     \\ \\_______\\ \\__\\\\ _\\\\ \\_______\\ \\_______\\ \\__\\ \\__\\ \\_______\\           \\ \\__\\ \\ \\_______\\____\\_\\  \\   \\ \\__\\       \\ \\__\\\\ _\\\\ \\_______\\ \\_______\\ \\__\\ \\__\\   \\ \\__\\ \\ \\__\\ \\__\\|\\__\\\n" +
                        "    \\|_______|\\|__|/       \\|_______|\\|__|\\|__|\\|_______|\\|_______|\\|__|\\|__|\\|_______|            \\|__|  \\|_______|\\_________\\   \\|__|        \\|__|\\|__|\\|_______|\\|_______|\\|__|\\|__|    \\|__|  \\|__|\\|__|\\|__|\n" +
                        "                                                                                                                   \\|_________|                                                                                  \n" +
                        "                                                                                                                                                                                                                 \n" +
                        "                                                                                                                                                                                                                 ");
                System.out.println("""
                        Este é um teste de sobrecarga, iremos capturar o uso de cpu e
                        informações acerca do seu disco e fazer uma comparação
                                                
                        Por favor informe a quantidade de pastas que você irá criar no computador
                        OBS: Não informe valores acima de 10 mil, do contrário você terá um árduo trabalho ao excluir todos
                        os arquivos.""");

                Scanner scanner = new Scanner(System.in);
                Integer totalArquivos = scanner.nextInt();
                Integer arquivos = 0;

                Double usoCpuInicial = looca.getProcessador().getUso();
                Long bytesEscritosInicial = looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeEscritas();
                Long bytesLidosInicial = looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeLeitura();

                System.out.println("""
                        Os dados atuais da sua máquina são:
                        Uso de CPU: %.2f
                        BytesEscritos: %d
                        BytesLidos: %d""".formatted(
                        usoCpuInicial, bytesEscritosInicial, bytesLidosInicial
                ));

                Long bytesEscritosAtual;
                Long bytesLidosAtual;
                Double usoCpuAtual;

                do {

                    usoCpuAtual = looca.getProcessador().getUso();
                    bytesEscritosAtual = looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeEscritas();
                    bytesLidosAtual = looca.getGrupoDeDiscos().getDiscos().get(0).getBytesDeLeitura();

                    String path1 = caminhoPasta + """
                            \\arquivo%d.txt""".formatted(arquivos); // Os arquivos só são criados com
                    // nomes diferentes, por isso o contador

                    FileWriter fw = new FileWriter(path1); // Serve para gravar caracteres em um arquivo
                    BufferedWriter bw = new BufferedWriter(fw); // Serve para escrever em um arquivo

                    bw.write("Uso de CPU atual: " + usoCpuAtual);
                    bw.newLine();
                    bw.write("Bytes escritos atualmente: " + bytesEscritosAtual);
                    bw.newLine();
                    bw.write("Bytes lidos atualmente: " + bytesLidosAtual);

                    arquivos++;

                    Double progresso = ((double) arquivos / totalArquivos * 100);
                    System.out.printf("\rProgresso atual: %.2f".formatted(progresso));

                    if (usoCpuAtual < usoCpuInicial) {
                        usoCpuAtual = usoCpuInicial;
                    }
                    bw.flush();
                    bw.close();
                } while (arquivos < totalArquivos);

                System.out.println("""
                        
                        Esse teste de sobrecarga tem o intuito de mostrar a importância de ter o monitoramento
                        preventivo em relação ao seu Hardware. Os dados gerados são a simulação de um processo
                        ETL em escala menor. Para aderir ao nosso projeto entre em contato conosco!
                        É possível acompanhar o aumento dos valores dentro de cada arquivo""");

                System.out.println("" +
                        """
                                
                                Seu maior número de Cpu atingido foi: %.2f
                                A quantidade de Bytes escritos após o teste foi: %d
                                A quantidade de Bytes lidos após o teste foi: %d""".formatted(
                                usoCpuAtual,
                                (bytesEscritosAtual - bytesEscritosInicial),
                                (bytesLidosAtual - bytesLidosInicial)
                        ));

            } catch (IOException e) { // Caso algo de errado na entrada e na saída ele não parará o
                // Código. Nesse caso ele só imprimirá um erro quando algo assim acontecer. A vantagem disso
                // Em em relação ao if e else é que ele já é totalmente abrangente.
                e.printStackTrace();
            }
        }
    }
