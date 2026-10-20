package bri;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/** Une connexion amateur : liste des services démarrés, choix, puis délégation au service. */
public class SessionAmateur implements Runnable {
    private final Socket socket;
    private final ServiceRegistry registry;

    public SessionAmateur(Socket socket, ServiceRegistry registry) {
        this.socket = socket;
        this.registry = registry;
    }

    @Override
    public void run() {
        try (Socket s = socket) {
            Echange e = new Echange(s);
            List<ServiceRegistry.Entree> liste = registry.demarres();
            if (liste.isEmpty()) {
                e.dire("Aucun service disponible pour le moment.");
                return;
            }
            StringBuilder menu = new StringBuilder("=== Services disponibles ===");
            for (int i = 0; i < liste.size(); i++)
                menu.append("\n").append(i + 1).append(" - ").append(liste.get(i).description);
            e.dire(menu.toString());

            int n;
            try {
                n = Integer.parseInt(e.demander("Votre choix :"));
            } catch (NumberFormatException ex) {
                e.dire("Choix invalide.");
                return;
            }
            if (n < 1 || n > liste.size()) {
                e.dire("Choix invalide.");
                return;
            }
            // instanciation par réflexion du service choisi, puis il prend la main sur la socket
            BRiService service = (BRiService) liste.get(n - 1).classe.getConstructor(Socket.class).newInstance(s);
            service.run();
        } catch (IOException ex) {
            System.err.println("[AMA] " + ex.getMessage());
        } catch (ReflectiveOperationException ex) {
            System.err.println("[AMA] impossible de lancer le service : " + ex);
        }
    }
}
