package org.example;

import org.json.JSONObject;

import java.io.IOException;

public class Teste {
    public static void main(String[] args) {

        try {
            JSONObject json = new JSONObject();
            json.put("text", "Aqui colocaremos os alertas!!");
            Slack.sendMessage(json);
        } catch (IOException e) {
            System.out.println("Deu ruim no slack" + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
