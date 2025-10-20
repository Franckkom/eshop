package server;

import common.net.Request;
import common.net.Response;
import logic.eShop;

import java.io.*;
import java.net.Socket;

/**
 * Die Klasse verarbeitet Anfragen eines einzelnen Clients über einen Socket
 * Sie liest die Request, leitet sie an den eShop zur Verarbeitung weiter und sendet dann das Response Objekt zurück
 * Die Kommunikation erfolgt über ObjektinputStream und ObjektOutputStream
 */

public class ClientRequestProcessor implements Runnable {

    private final Socket socket;
    private final eShop shop;

    public ClientRequestProcessor(Socket socket, eShop shop) {
        this.socket = socket;
        this.shop = shop;
    }

    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof Request req) {
                    Response res = shop.dispatch(req);
                    out.writeObject(res);
                    out.flush();

                    if (req.getCommand().name().equals("QUIT")) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler bei der Client Verbindung: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Client getrennt: " + socket);
            } catch (IOException ignored) {}
        }
    }
}
