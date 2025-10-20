package common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Diese Klasse definiert den Warenkorb
 */

public class Warenkorb implements Serializable{
    private Map<Artikel, Integer> artikelMap = new HashMap<>(); // speichert Artikel und deren Anzahl

    /**
     * Fügt einen Artikel mit einer bestimmten Stückanzahl zum Warenkorb hinzu.
     * Wenn der Artikel bereits existiert wird die Gesamtmenge erhöht.
     */
    public void artikelHinzufuegen(Artikel artikel, int menge) {
        artikelMap.put(artikel, artikelMap.getOrDefault(artikel, 0) + menge);
    }


    public void artikelEntfernen(Artikel artikel) {
        artikelMap.remove(artikel);
    }


    public void leeren() {
        artikelMap.clear();
    }

    /**
     * Gibt die aktuelle Liste aller Artikel und Mengen im Warenkorb zurück für die Rechnung später.
     */
    public Map<Artikel, Integer> getArtikelMap() {
        return artikelMap;
    }

    /**
     * Gesamtpreis aller ausgewählten Artikel wird berechnet
     */
    public double berechneGesamtpreis() {
        double summe = 0;
        for (Map.Entry<Artikel, Integer> eintrag : artikelMap.entrySet()) {
            summe += eintrag.getKey().getPreis() * eintrag.getValue();
        }
        return summe;
    }


    public boolean istLeer() {
        return artikelMap.isEmpty();
    }
}
