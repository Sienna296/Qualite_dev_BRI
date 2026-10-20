package bri;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Comptes programmeurs. La certification BRi est automatique à la création. */
public class ProgrammeurManager {
    private final Map<String, Programmeur> programmeurs = new ConcurrentHashMap<>();

    /** Le login sert de nom de package : il doit donc être un identifiant Java en minuscules. */
    public boolean creer(String login, String mdp, String ftpUrl) {
        if (!login.matches("[a-z][a-z0-9_]*") || mdp.isEmpty() || ftpUrl.isEmpty()) return false;
        return programmeurs.putIfAbsent(login, new Programmeur(login, mdp, ftpUrl)) == null;
    }

    public Programmeur authentifier(String login, String mdp) {
        Programmeur p = programmeurs.get(login);
        return (p != null && p.verifierMdp(mdp)) ? p : null;
    }
}
