package client;

import common.model.*;
import common.net.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Die OnlineShopFassade kapselt die Netzwerkkommunikation mit dem Server.
 * Schnittstelle zur Nutzung der Funktionen des Online-Shops
 * Anfragen werden als request über Sockets versendet und als response zurückgegeben
 */

public class OnlineShopFassade {

    private final String host;
    private final int port;

    public OnlineShopFassade(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Response sendeRequest(Request req) {
        try (
                Socket socket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.writeObject(req);
            out.flush();

            Object resObj = in.readObject();
            if (resObj instanceof Response res) {
                return res;
            } else {
                throw new RuntimeException("Ungültige Antwort vom Server");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Kommunikationsfehler: " + e.getMessage(), e);
        }
    }


    // Test zur Verbindung
    public boolean testverbindung() {
        try {
            sendeRequest(new Request(Command.QUIT, null));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Benutzerverwaltung
    public Benutzer login(String benutzername, String passwort) {
        Object[] daten = {benutzername, passwort};
        Response r = sendeRequest(new Request(Command.LOGIN, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (Benutzer) r.getData();
    }

    public void registriereKunde(String name, String land, String stadt, String plz, String str, String user, String pw) {
        Object[] daten = {name, land, stadt, plz, str, user, pw};
        Response r = sendeRequest(new Request(Command.REGISTRIERE_KUNDE, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }

    public void registriereMitarbeiter(String name, String user, String pw) {
        Object[] daten = {name, user, pw};
        Response r = sendeRequest(new Request(Command.REGISTRIERE_MITARBEITER, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }


    // Artikelverwaltung
    public List<Artikel> zeigeAlleArtikel() {
        Response r = sendeRequest(new Request(Command.ALLE_ARTIKEL, null));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (List<Artikel>) r.getData();
    }

    public void artikelAnlegen(int nummer, String bezeichnung, int bestand, double preis, Mitarbeiter mitarbeiter) {
        Object[] daten = {nummer, bezeichnung, bestand, preis, mitarbeiter};
        Response r = sendeRequest(new Request(Command.ARTIKEL_ANLEGEN, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }

    public void massengutArtikelAnlegen(int nummer, String bezeichnung, int bestand, double preis, int packungsgroesse, Mitarbeiter mitarbeiter) {
        Object[] daten = {nummer, bezeichnung, bestand, preis, packungsgroesse, mitarbeiter};
        Response r = sendeRequest(new Request(Command.MASSENGUT_ANLEGEN, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }

    public void bestandAendern(int artikelnummer, int neuerBestand, Mitarbeiter mitarbeiter) {
        Object[] daten = {artikelnummer, neuerBestand, mitarbeiter};
        Response r = sendeRequest(new Request(Command.BESTAND_AENDERN, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }


    // Warenkorb
    public void artikelZumWarenkorb(Kunde kunde, int artikelnummer, int menge) {
        Object[] daten = {kunde, artikelnummer, menge};
        Response r = sendeRequest(new Request(Command.WARENKORB_HINZUFUEGEN, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }

    public Map<Artikel, Integer> warenkorbInhalt(Kunde kunde) {
        Response r = sendeRequest(new Request(Command.WARENKORB_INHALT, kunde));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (Map<Artikel, Integer>) r.getData();
    }

    public double warenkorbSumme(Kunde kunde) {
        Response r = sendeRequest(new Request(Command.WARENKORB_SUMME, kunde));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (Double) r.getData();
    }

    public void warenkorbMengeAendern(Kunde kunde, int artikelnummer, int menge) {
        Object[] daten = {kunde, artikelnummer, menge};
        Response r = sendeRequest(new Request(Command.WARENKORB_MENGE_AENDERN, daten));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }

    public void warenkorbLeeren(Kunde kunde) {
        Response r = sendeRequest(new Request(Command.WARENKORB_LEEREN, kunde));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
    }

    public List<String> kaufAbschliessen(Kunde kunde) {
        Response r = sendeRequest(new Request(Command.KAUF_ABSCHLIESSEN, kunde));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (List<String>) r.getData();
    }


    // Ereignisse und Bestand
    public List<Ereignis> getEreignisse() {
        Response r = sendeRequest(new Request(Command.EREIGNISSE, null));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (List<Ereignis>) r.getData();
    }

    public List<String> getBestandshistorie(int artikelnummer) {
        Response r = sendeRequest(new Request(Command.BESTANDSHISTORIE, artikelnummer));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (List<String>) r.getData();
    }

    public List<Integer> getBestandswerte(int artikelnummer) {
        Response r = sendeRequest(new Request(Command.BESTANDSWERTE, artikelnummer));
        if (!r.isSuccess()) throw new RuntimeException(r.getMessage());
        return (List<Integer>) r.getData();
    }
}



