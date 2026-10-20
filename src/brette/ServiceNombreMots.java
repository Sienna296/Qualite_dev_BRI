package brette;

import bri.BRiService;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServiceNombreMots implements BRiService {
    private final Socket client;

    public ServiceNombreMots(Socket client) { this.client = client; }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            out.println("> Texte à analyser :");
            String texte = in.readLine();
            int n = (texte == null || texte.isBlank()) ? 0 : texte.trim().split("\\s+").length;
            out.println("Résultat : " + n + " mot(s)");
        } catch (IOException e) {
            System.err.println("ServiceNombreMots : " + e.getMessage());
        }
    }

    public static String toStringue() { return "Comptage des mots d'un texte"; }
}
