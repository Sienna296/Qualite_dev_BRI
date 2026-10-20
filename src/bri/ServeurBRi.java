package bri;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

/** Serveur TCP générique : un thread par client, la session est fabriquée par 'fabrique'. */
public class ServeurBRi implements Runnable, Closeable {
    private final ServerSocket serveur;
    private final Function<Socket, Runnable> fabrique;

    public ServeurBRi(int port, Function<Socket, Runnable> fabrique) throws IOException {
        this.serveur = new ServerSocket(port);
        this.fabrique = fabrique;
    }

    @Override
    public void run() {
        System.out.println("Serveur en écoute sur le port " + serveur.getLocalPort());
        try {
            while (!serveur.isClosed()) {
                Socket client = serveur.accept();
                new Thread(fabrique.apply(client)).start();
            }
        } catch (IOException e) {
            if (!serveur.isClosed()) System.err.println("Serveur arrete : " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException { serveur.close(); }
}
