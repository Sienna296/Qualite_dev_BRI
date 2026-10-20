package bri;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Liste partagée des services (accédée par tous les threads : méthodes synchronized). */
public class ServiceRegistry {

    public static class Entree {
        public final String nom;            // nom complet : login.NomClasse
        public final String proprietaire;
        public final String description;
        public final Class<?> classe;
        private final URLClassLoader loader;
        private boolean demarre = true;

        public Entree(String nom, String proprietaire, String description, Class<?> classe, URLClassLoader loader) {
            this.nom = nom;
            this.proprietaire = proprietaire;
            this.description = description;
            this.classe = classe;
            this.loader = loader;
        }

        void fermer() {
            try { loader.close(); } catch (IOException ignore) { }
        }
    }

    private final Map<String, Entree> services = new LinkedHashMap<>();

    public synchronized void installer(Entree e) throws BRiException {
        if (services.containsKey(e.nom)) {
            e.fermer();
            throw new BRiException("Service déjà installé : " + e.nom + " (utilisez la mise à jour)");
        }
        services.put(e.nom, e);
    }

    /** Remplace la classe par une nouvelle version (nouveau ClassLoader), sans redémarrer le serveur. */
    public synchronized void mettreAJour(Entree e) throws BRiException {
        Entree ancien = services.get(e.nom);
        if (ancien == null || !ancien.proprietaire.equals(e.proprietaire)) {
            e.fermer();
            throw new BRiException("Aucun service à vous nommé " + e.nom);
        }
        e.demarre = ancien.demarre;
        services.put(e.nom, e);
        // l'ancien loader n'est pas fermé : des clients peuvent encore utiliser l'ancienne version
    }

    public synchronized void demarrer(String nom, String login) throws BRiException {
        possede(nom, login).demarre = true;
    }

    public synchronized void arreter(String nom, String login) throws BRiException {
        possede(nom, login).demarre = false;
    }

    public synchronized void desinstaller(String nom, String login) throws BRiException {
        possede(nom, login);
        services.remove(nom);
    }

    /** Seuls les services démarrés sont proposés aux amateurs. */
    public synchronized List<Entree> demarres() {
        List<Entree> liste = new ArrayList<>();
        for (Entree e : services.values()) if (e.demarre) liste.add(e);
        return liste;
    }

    private Entree possede(String nom, String login) throws BRiException {
        Entree e = services.get(nom);
        if (e == null || !e.proprietaire.equals(login)) throw new BRiException("Aucun service à vous nommé " + nom);
        return e;
    }
}
