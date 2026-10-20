package brette;   // = le login du programmeur (à renommer avec le tien)

import bri.BRiService;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServiceInversion implements BRiService {
    private final Socket client;

    public ServiceInversion(Socket client) { this.client = client; }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            out.println("> Texte à inverser :");
            String texte = in.readLine();
            out.println("Résultat : " + new StringBuilder(texte == null ? "" : texte).reverse());
        } catch (IOException e) {
            System.err.println("ServiceInversion : " + e.getMessage());
        }
    }

    public static String toStringue() { return "Inversion d'un texte"; }
}
