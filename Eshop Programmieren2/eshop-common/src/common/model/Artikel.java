package common.model;
import java.io.Serializable;

/**
 * Diese Klasse definiert die Artikel
 */


public class Artikel implements Serializable{
    private int artikelnummer;
    private String bezeichnung;
    private int bestand;
    private double preis;

    /**
     * Konstruktor
     * @param artikelnummer die eindeutige Nummer
     * @param bezeichnung der Name des Artikels
     * @param bestand wie viele Stücke es gibt
     * @param preis der Preis pro Stück
     */
    public Artikel(int artikelnummer, String bezeichnung, int bestand, double preis) {
        this.artikelnummer = artikelnummer;
        this.bezeichnung = bezeichnung;
        this.bestand = bestand;
        this.preis = preis;
    }

    public int getArtikelnummer() {
        return artikelnummer;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public int getBestand() {
        return bestand;
    }

    public double getPreis() {
        return preis;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    // Beschreibung des Artikels als Text
    @Override
    public String toString() {
        return artikelnummer + " - " + bezeichnung + " (" + bestand + " Stück, " + preis + " €)";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Artikel other)) return false;
        return this.artikelnummer == other.artikelnummer;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(artikelnummer);
    }
}
