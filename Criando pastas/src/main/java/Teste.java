import java.io.*;

public class Teste {
    public static void main(String[] args) throws IOException {
       String caminho = ("C:\\Users\\luisg\\Documents"); // Caminho onde a pasta será criada no seu PC
       String nomeArquivo = "Teste"; // Nome da pasta que será criada
       String caminhoPasta = caminho + "\\" + nomeArquivo; // O caminho da pasta para criá-la

       File dir = new File(caminhoPasta); // A classe File é genérica, serve para criar um arquivo
       dir.mkdir(); // Nesse caso mkdir, um diretório está sendo criado

        try{ // Bloco de execução para lançar uma excecção
            Integer arquivos = 0; // Número de arquivos
            do {
                String path1 = caminhoPasta + """
                        \\arquivo%d.txt""".formatted(arquivos); // Os arquivos só são criados com
                // nomes diferentes, por isso o contador
                FileWriter fw = new FileWriter(path1); // Criador do FileWriter, ele é um "escritor de
                // arquivos"
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Escrevendo texto");
                bw.newLine();
                bw.write("Texto de novo");
                bw.newLine();
                bw.write("Terceira linha");
                arquivos++;
                bw.flush();
                bw.close();
            } while (arquivos < 5000000);
        } catch (IOException e) { // Caso algo de errado na entrada e na saída ele não parará o
            // Código. Nesse caso ele só imprimirá um erro quando algo assim acontecer. A vantagem disso
            // Em em relação ao if e else é que ele já é totalmente abrangente.
            e.printStackTrace();
        }
    }
}
