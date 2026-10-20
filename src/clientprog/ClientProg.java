package clientprog;

import client.ClientBase;

public class ClientProg {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 3001;
        ClientBase.lancer(host, port);
    }
}
