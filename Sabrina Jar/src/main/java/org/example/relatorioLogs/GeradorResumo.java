package org.example.relatorioLogs;

import org.example.logging.TagNiveisLog;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GeradorResumo {
    private static int totalLogs = 0;
    private static int totalErros = 0;
    private static int totalInformativos = 0;
    private static int totalAvisos = 0;
    static LocalDateTime dataAtual = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static String dataResumo = dataAtual.format(formatter);

    public static void atualizarEstatisticas(TagNiveisLog tag){
        totalLogs++;
        switch (tag) {
            case ERROR -> totalErros++;
            case INFO -> totalInformativos++;
            case WARN -> totalAvisos++;
            default -> System.out.println("Opção inválida.");
        }
    }

    public static void gerarArquivoResumo(){
        String dirResumo = "classes/org/example/logging/ResumoLogs.txt";
        try {
            PrintWriter out = new PrintWriter(new FileWriter(dirResumo));
            out.println("---------------------------------------");
            out.println("Resumo dos Logs - Data: " + dataResumo);
            out.println("Total de logs: " + totalLogs);
            out.println("Logs de Erros: " + totalErros);
            out.println("Logs Informativos: " + totalInformativos);
            out.println("Logs de Avisos: " + totalAvisos);
            out.println("---------------------------------------");
            out.println("Análise das estatísticas");

            Double percetualErros = ((double) totalErros / totalLogs) * 100;
            Double percentualAvisos = ((double) totalAvisos / totalLogs) * 100;

            if(percentualAvisos > 0.10){
                out.println("Atenção! Logs recorrentes de alerta indicam movimentações inesperadas nos dados capturados!");
            } else if(percetualErros > 0.25){
                out.println("Atenção! Logs recorrentes de erros indicam irregularidades com a aplicação. Reinicie o processo de captura.");
            } else {
                out.println("O monitoramento de seus equipamentos estão sob controle com a Re;Data! :)");
            }
            out.close();

            System.out.println("Arquivo de resumos do log foi gerado no diretório atual logs!");
        } catch (IOException e) {
            System.out.println("Houve um erro ao gerar o arquivo de resumo!");
            e.printStackTrace();
        }
    }
}
