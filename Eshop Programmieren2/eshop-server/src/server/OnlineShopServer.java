package server;

import logic.eShop;
import logic.persistence.DatenManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Start des Servers
 * Diese Klasse startet einen TCP-Server mit Port 12345
 * Akzeptiert Client-Verbindungen und verarbeitet sie in separaten Threads über den ClientRequestProcessor
 * Lädt Daten beim Start und speichert sie beim Beenden
 */

public class OnlineShopServer {

    public static final int PORT = 12345;

    public static void main(String[] args) {
        eShop shop = new eShop();

        // Daten laden beim Start
        try {
            DatenManager.laden(shop);
            System.out.println("Daten erfolgreich geladen.");
        } catch (Exception e) {
            System.out.println("Daten konnten nicht geladen werden: " + e.getMessage());
        }

        // Admin erstellen mit Benutzername: admin1, password:111
        try {
            boolean adminVorhanden = shop.getMitarbeiter().stream()
                    .anyMatch(m -> m.getBenutzername().equalsIgnoreCase("admin1"));

            if (!adminVorhanden) {
                shop.registriereMitarbeiter("Admin", "admin1", "111");
                System.out.println("Standard-Admin 'admin1' wurde neu angelegt.");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Anlegen des Admins: " + e.getMessage());
        }

        // Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DatenManager.speichern(shop);
                System.out.println("Daten gespeichert.");
            } catch (Exception e) {
                System.err.println("Fehler beim Speichern: " + e.getMessage());
            }
        }));

        // Server starten
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server läuft mit Port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client verbunden: " + clientSocket);

                ClientRequestProcessor handler = new ClientRequestProcessor(clientSocket, shop);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("Server Problem: " + e.getMessage());
        }
    }
}


