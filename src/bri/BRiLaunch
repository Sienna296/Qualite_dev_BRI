package bri;

import java.io.IOException;

/** Point d'entrée du serveur : un port pour les amateurs, un port pour les programmeurs. */
public class BRiLaunch {
    public static final int PORT_AMA = 3000;
    public static final int PORT_PROG = 3001;

    public static void main(String[] args) throws IOException {
        ProgrammeurManager programmeurs = new ProgrammeurManager();
        ServiceRegistry registry = new ServiceRegistry();

        new Thread(new ServeurBRi(PORT_AMA, s -> new SessionAmateur(s, registry)), "serveur-ama").start();
        new Thread(new ServeurBRi(PORT_PROG, s -> new SessionProgrammeur(s, programmeurs, registry)), "serveur-prog").start();
        System.out.println("BRiLaunch démarré (amateurs : " + PORT_AMA + ", programmeurs : " + PORT_PROG + ")");
    }
}
