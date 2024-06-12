    package org.example.logging;

    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.net.InetAddress;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.attribute.BasicFileAttributes;
    import java.text.SimpleDateFormat;
    import java.time.Instant;
    import java.time.temporal.ChronoUnit;
    import java.util.Date;
    import java.util.Timer;
    import java.util.TimerTask;


    public class GeradorLog {
        private final static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        private final static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        private final static String currentDateTime = formatDateTime.format(new Date());
        private final static String currentDate = formatDate.format(new Date());
        static String logFileName = "log" + currentDate + ".txt";
        private static final String LOG_DIR = "src/main/java/org/example/logging/";
        private static final String LOG_FILE = LOG_DIR + logFileName;

        public static void log(TagNiveisLog tag, String message, Modulo module){
            try {
                String hostName = InetAddress.getLocalHost().getHostName();

                PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true));

                out.println(currentDateTime + " - " + tag.getDescricao() + " - " + module.getID() + " - " + hostName + " - " + message);

                out.close();
            } catch (IOException e){
                System.out.println("Houve um erro ao registrar o log!");
                e.printStackTrace();
            }
        }

        public static void cleanerOldLogs(int tempo, int i, ChronoUnit unidade) {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists() || !logDir.isDirectory()) {
                System.out.println("Diretório de logs não encontrado: " + LOG_DIR);
                return;
            }

            File[] logFiles = logDir.listFiles();
            if (logFiles == null) {
                System.out.println("Nenhum arquivo de log encontrado no diretório: " + LOG_DIR);

            }

            Instant limite = Instant.now().minus(tempo, unidade);

            for (File logFile : logFiles) {
                try {
                    Path filePath = logFile.toPath();
                    BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
                    Instant fileTime = attrs.creationTime().toInstant();

                    if (fileTime.isBefore(limite)) {
                        if (logFile.delete()) {
                            System.out.println("Arquivo de log excluído: " + logFile.getName());
                        } else {
                            System.out.println("Falha ao excluir o arquivo de log: " + logFile.getName());
                        }
                    }
                } catch (IOException e) {
                    log(TagNiveisLog.ERROR, "Erro ao acessar atributos do arquivo.", Modulo.ALERTA);
                }
            }
        }

        public static void autoClean(Long tempo, int intervaloMinutos, ChronoUnit unidade) {
            Timer timer = new Timer(true);
            TimerTask tarefaLimpeza = new TimerTask() {
                @Override
                public void run() {
                    cleanerOldLogs(Math.toIntExact(tempo), 2, unidade);
                }
            };
            timer.scheduleAtFixedRate(tarefaLimpeza, 0, (long) intervaloMinutos * 60 * 1000);
        }
    }
