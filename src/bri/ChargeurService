package bri;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

/** Va chercher une classe sur le serveur ftp du programmeur et contrôle la norme BRi. */
public class ChargeurService {

    public static String nomComplet(String login, String saisie) {
        return saisie.startsWith(login + ".") ? saisie : login + "." + saisie;
    }

    public static ServiceRegistry.Entree charger(Programmeur p, String saisie) throws BRiException {
        String login = p.getLogin();
        String nom = nomComplet(login, saisie);
        String simple = nom.substring(login.length() + 1);
        // contrôle : la classe doit être directement dans le package = login
        if (!simple.matches("[A-Za-z_$][A-Za-z0-9_$]*"))
            throw new BRiException("La classe doit être dans le package '" + login + "' (sans sous-package)");

        URLClassLoader loader = null;
        try {
            String base = p.getFtpUrl();
            if (!base.endsWith("/")) base += "/";
            // le parent = loader du serveur : BRiService est ainsi la MÊME classe des deux côtés
            loader = new URLClassLoader(new URL[]{java.net.URI.create(base).toURL()}, BRiService.class.getClassLoader());
            Class<?> c = loader.loadClass(nom);
            String description = verifierNorme(c);
            return new ServiceRegistry.Entree(nom, login, description, c, loader);
        } catch (MalformedURLException | IllegalArgumentException e) {
            fermer(loader);
            throw new BRiException("URL ftp invalide : " + p.getFtpUrl());
        } catch (ClassNotFoundException e) {
            fermer(loader);
            throw new BRiException("Classe introuvable sur votre serveur : " + nom);
        } catch (LinkageError e) {
            fermer(loader);
            throw new BRiException("Classe illisible : " + e);
        } catch (BRiException e) {
            fermer(loader);
            throw e;
        }
    }

    private static String verifierNorme(Class<?> c) throws BRiException {
        if (!BRiService.class.isAssignableFrom(c))
            throw new BRiException("La classe doit implémenter bri.BRiService");
        int m = c.getModifiers();
        if (c.isInterface() || Modifier.isAbstract(m) || !Modifier.isPublic(m))
            throw new BRiException("La classe doit être publique et concrète");
        try {
            c.getConstructor(Socket.class);
        } catch (NoSuchMethodException e) {
            throw new BRiException("Constructeur public (Socket) manquant");
        }
        try {
            Method t = c.getMethod("toStringue");
            if (!Modifier.isStatic(t.getModifiers()) || t.getReturnType() != String.class) throw new NoSuchMethodException();
            return (String) t.invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new BRiException("Méthode 'public static String toStringue()' manquante ou en erreur");
        }
    }

    private static void fermer(URLClassLoader l) {
        try { if (l != null) l.close(); } catch (Exception ignore) { }
    }
}
