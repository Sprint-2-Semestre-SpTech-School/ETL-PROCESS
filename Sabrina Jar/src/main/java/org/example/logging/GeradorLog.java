    package org.example.logging;

    import java.io.FileWriter;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.net.InetAddress;
    import java.text.SimpleDateFormat;
    import java.util.Date;

    public class GeradorLog {
        private final static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        private final static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        private final static String currentDateTime = formatDateTime.format(new Date());
        private final static String currentDate = formatDate.format(new Date());
        static String logFileName = "log" + currentDate + ".txt";
        private static final String LOG_FILE = "src/main/java/org/example/logging/" + logFileName;

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
    }
