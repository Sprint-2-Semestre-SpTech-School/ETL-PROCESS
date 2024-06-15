package org.example;

import java.util.Scanner;

public class JarJulia {

        Scanner inputLine = new Scanner(System.in);
        String processoEtl;
        String extracao = "extração";
        String transformacao = "transformação";
        String carregamento = "carregamento";


    public void escolherProcesso() {
            System.out.println("""
            Olá, seja bem-vindo!
            
            Para iniciarmos nosso processo, me conte qual etapa do processo ETL essa máquina irá realizar""");
            processoEtl = inputLine.nextLine();

            while (!(processoEtl.equalsIgnoreCase(extracao) ||
                    !(processoEtl.equalsIgnoreCase(transformacao) || !(processoEtl.equalsIgnoreCase(carregamento))))) {
                System.out.println("\nDigite um processo válido");

                System.out.println("Insira aqui o processo desejado:");
                processoEtl = inputLine.nextLine();
            }

            validarProcessos();
        }

        public void validarProcessos() {
            Cpu cpu = new Cpu();
            Disco disco = new Disco();
            Ram ram = new Ram();
            Rede rede = new Rede();

//            cpu.inserirDados();
//            rede.inserirDados();
//            disco.inserirDados();

            if (processoEtl.equalsIgnoreCase("extração")) {


                System.out.println("""
                    O principal componente a ser monitorado é:
                    - Rede:  se a largura de banda da rede for limitada, pode haver uma maior demora na extração de grandes volumes de dados
                    
                    - Disco: se o disco de destino estiver lento ou sobrecarregado, esse processo pode ser significativamente prejudicado, resultando em tempos de carga mais longos.""\");
                    
                    Segue os dados da Rede:""");

                rede.capturarDados();
                System.out.println();

            } else if (processoEtl.equalsIgnoreCase("transformação")) {

                System.out.println("""
                    - CPU: caso ela esteja sobrecarregada, o desempenho do processo pode diminuir significativamente,
                    resultando em tempos de transformação mais longos e atrasos na conclusão do processo
                    
                    - Memória RAM: a quantidade disponível determina a capacidade do sistema de manipular grandes volumes de dados na memória.
                    
                    - Disco: se o disco de destino estiver lento ou sobrecarregado, esse processo pode ser significativamente prejudicado, resultando em tempos de carga mais longos.
                    
                    Segue os dados da CPU:""");

                cpu.capturarDados();
                System.out.println();

            } else if(processoEtl.equalsIgnoreCase("carregamento")) {

                System.out.println("""
                    - Rede: se a rede for lenta ou instável, pode haver atrasos na transferência de dados entre os sistemas de origem e destino
                    
                    - Disco: se o disco de destino estiver lento ou sobrecarregado, esse processo pode ser significativamente prejudicado, resultando em tempos de carga mais longos.
                    
                    Segue os dados do Disco:""");

                disco.capturarDados();
                System.out.println();

            } else {
                System.out.println("""
                    Digite um processo válido.""");
            }
        }
    }