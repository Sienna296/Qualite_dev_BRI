package bri;

import java.io.IOException;
import java.net.Socket;

/** Une connexion programmeur : authentification puis gestion de ses services. */
public class SessionProgrammeur implements Runnable {
    private static final String MENU = "=== Menu programmeur ===\n"
            + "1 - Fournir un nouveau service\n"
            + "2 - Mettre à jour un service\n"
            + "3 - Changer l'adresse de mon serveur ftp\n"
            + "4 - Demarrer un service\n"
            + "5 - Arreter un service\n"
            + "6 - Desinstaller un service\n"
            + "0 - Quitter";

    private final Socket socket;
    private final ProgrammeurManager programmeurs;
    private final ServiceRegistry registry;

    public SessionProgrammeur(Socket socket, ProgrammeurManager programmeurs, ServiceRegistry registry) {
        this.socket = socket;
        this.programmeurs = programmeurs;
        this.registry = registry;
    }

    @Override
    public void run() {
        try (Socket s = socket) {
            Echange e = new Echange(s);
            Programmeur p = authentifier(e);
            if (p != null) boucle(e, p);
        } catch (IOException ex) {
            System.err.println("[PROG] " + ex.getMessage());
        }
    }

    private Programmeur authentifier(Echange e) throws IOException {
        e.dire("=== BRiLaunch : espace programmeur ===\n1 - Se connecter\n2 - Creer un compte");
        String choix = e.demander("Votre choix :");
        String login = e.demander("Login :");
        String mdp = e.demander("Mot de passe :");
        if (choix.equals("2")) {
            String ftp = e.demander("URL de votre serveur ftp (ex: ftp://monserveur/classes/) :");
            if (!programmeurs.creer(login, mdp, ftp)) {
                e.dire("Création impossible (login invalide ou déjà pris).");
                return null;
            }
            e.dire("Compte créé.");
        }
        Programmeur p = programmeurs.authentifier(login, mdp);
        if (p == null) {
            e.dire("Échec d'authentification.");
            return null;
        }
        e.dire("Bienvenue " + login + " !");
        return p;
    }

    private void boucle(Echange e, Programmeur p) throws IOException {
        while (true) {
            e.dire(MENU);
            String choix = e.demander("Votre choix :");
            try {
                switch (choix) {
                    case "1": {
                        String nom = e.demander("Nom de la classe (sans package) :");
                        registry.installer(ChargeurService.charger(p, nom));
                        e.dire("Service installe et demarre.");
                        break;
                    }
                    case "2": {
                        String nom = e.demander("Nom de la classe a mettre à jour :");
                        registry.mettreAJour(ChargeurService.charger(p, nom));
                        e.dire("Service mis à jour.");
                        break;
                    }
                    case "3": {
                        p.setFtpUrl(e.demander("Nouvelle URL ftp :"));
                        e.dire("Adresse ftp modifiee.");
                        break;
                    }
                    case "4": {
                        registry.demarrer(ChargeurService.nomComplet(p.getLogin(), e.demander("Nom du service :")), p.getLogin());
                        e.dire("Service démarre.");
                        break;
                    }
                    case "5": {
                        registry.arreter(ChargeurService.nomComplet(p.getLogin(), e.demander("Nom du service :")), p.getLogin());
                        e.dire("Service arrete.");
                        break;
                    }
                    case "6": {
                        registry.desinstaller(ChargeurService.nomComplet(p.getLogin(), e.demander("Nom du service :")), p.getLogin());
                        e.dire("Service desinstalle.");
                        break;
                    }
                    case "0":
                        e.dire("Au revoir.");
                        return;
                    default:
                        e.dire("Choix invalide.");
                }
            } catch (BRiException ex) {
                e.dire("Erreur : " + ex.getMessage());
            }
        }
    }
}
