package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Slack {
    private static HttpClient cliente = HttpClient.newHttpClient();
    // "cliente" HTTP responsável por realizar as requisições
    private static final String link = "https://hooks.slack.com/services/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";
    // Esse Link de cima é o link gerado no app Slack configurado (o Nome do robo de alerta é "O Alertador")

    public static void sendMessage(JSONObject content) throws IOException, InterruptedException {
        HttpRequest requisicao = HttpRequest.newBuilder(
                        URI.create(link))
                .header("accept", "application/json") // <-- valores do header da requisição
                .POST(HttpRequest.BodyPublishers.ofString(content.toString())) // <-- método .POST para postar informações no canal
                .build(); // <-- armazena todas as informações no "requisicao"(HttpRequest)

        HttpResponse<String> response = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());

        System.out.println("""
                Status: %s""".formatted(response.statusCode()));
        System.out.println("""
                Response: %s""".formatted(response.body()));
    }

}
