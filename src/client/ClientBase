package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/** Client texte générique : affiche ce que dit le serveur, répond quand une ligne commence par '>'. */
public class ClientBase {
    public static void lancer(String host, int port) {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        try (Socket s = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in))) {
            String ligne;
            while ((ligne = in.readLine()) != null) {
                if (ligne.startsWith(">")) {
                    System.out.print(ligne.substring(1).replace("##", "\n") + " ");
                    String reponse = clavier.readLine();
                    if (reponse == null) break;
                    out.println(reponse);
                } else {
                    System.out.println(ligne.replace("##", "\n"));
                }
            }
        } catch (IOException e) {
            System.err.println("Connexion impossible : " + e.getMessage());
        }
    }
}
