package bri;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Petit protocole texte, une ligne = un message :
 *  - "##" remplace les retours à la ligne dans un message
 *  - une ligne qui commence par ">" est une question : le client doit répondre
 */
public class Echange {
    private final BufferedReader in;
    private final PrintWriter out;

    public Echange(Socket s) throws IOException {
        in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    public void dire(String message) {
        out.println(message.replace("\n", "##"));
    }

    public String demander(String question) throws IOException {
        out.println("> " + question);
        String reponse = in.readLine();
        if (reponse == null) throw new IOException("Client déconnecté");
        return reponse.trim();
    }
}
