package windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Inovacao usbReader = new Inovacao();
        // Criação da instância do timer
        Timer timer = new Timer();
        // Criaçaõ da instância da tarefa
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() { // Tudo dentro desse run é a minha tarefa
                // Cria o ProcessBuilder, o primeiro parâmetro é o que irá ser executado
                // O segundo significa que é um comando que deve ser executado
                // O terceiro o próprio comando em si.
                ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "/c",
                        usbReader.listarDescricao());

                // Iniciar o processo
                Process process;

                List<String> descricaoDispositivo = new ArrayList<>();
                List<String> idDispositivo = new ArrayList<>();
                try {
                    process = processBuilder.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Cria um leitor para ler o processo
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // String linha e um while para ler linha a linha, quando acabar, encerra o código
                String line;
                try{
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    System.out.println("Comando finalizado.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }; // Aqui é o fim do bloco da tarefa
        timer.schedule(tarefa, 1000, 2000); // Aqui definimos o parâmetro da tarefa, o primeiro
        // Parâmetro é a própria tarefa, o segundo é o delay para executar e o último é o período no qual
        // O bloco será executado.
    }
}
