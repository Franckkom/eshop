package logic.persistence;

import logic.eShop;
import common.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse dient dem Laden und Speichern
 * Die Daten werden im lokalen Dateisystem im Serialisierungsformat gespeichert
 */

public class DatenManager {
    private static final String PFAD_ARTIKEL = "artikel.ser";
    private static final String PFAD_KUNDEN = "kunden.ser";
    private static final String PFAD_MITARBEITER = "mitarbeiter.ser";
    private static final String PFAD_EREIGNISSE = "ereignisse.ser";

    //speichern der Daten
    public static void speichern(eShop shop) throws IOException {
        speichernListe(shop.getArtikelverwaltung().getAlleArtikel(), PFAD_ARTIKEL);
        speichernListe(shop.getKunden(), PFAD_KUNDEN);
        speichernListe(shop.getMitarbeiter(), PFAD_MITARBEITER);
        speichernListe(shop.getEreignisse(), PFAD_EREIGNISSE);
    }

    //Laden der Daten
    public static void laden(eShop shop) throws IOException, ClassNotFoundException {
        List<Artikel> artikel = ladenListe(PFAD_ARTIKEL);
        List<Kunde> kunden = ladenListe(PFAD_KUNDEN);
        List<Mitarbeiter> mitarbeiter = ladenListe(PFAD_MITARBEITER);
        List<Ereignis> ereignisse = ladenListe(PFAD_EREIGNISSE);

        shop.getArtikelverwaltung().setAlleArtikel(artikel);
        shop.setKunden(kunden);
        shop.setMitarbeiter(mitarbeiter);
        shop.setEreignisse(ereignisse);
    }

    private static <T> void speichernListe(List<T> liste, String dateiname) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dateiname))) {
            oos.writeObject(liste);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> ladenListe(String dateiname) throws IOException, ClassNotFoundException {
        File file = new File(dateiname);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        }
    }
}

