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
        String dirResumo = "src/main/java/org/example/logging/resumoLogs.txt";
        try {
            PrintWriter out = new PrintWriter(new FileWriter(dirResumo));
            out.println("Resumo dos Logs - Data: " + dataResumo);
            out.println("Total de logs: " + totalLogs);
            out.println("Erros: " + totalErros);
            out.println("Informativos: " + totalInformativos);
            out.println("Avisos: " + totalAvisos);
            out.close();
        } catch (IOException e) {
            System.out.println("Houve um erro ao gerar o arquivo de resumo!");
            e.printStackTrace();
        }
    }
}
