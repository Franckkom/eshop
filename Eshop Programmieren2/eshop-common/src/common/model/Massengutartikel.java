package common.model;

/**
 * Diese Klasse erbt von Artikel und definiert die Größenanzahl der Massenartikel
 */


public class Massengutartikel extends Artikel {
    private int packungsgroesse;

    /**
     * Konstruktor für Massengutartikel
     * @param artikelnummer eindeutige Artikelnummer
     * @param bezeichnung Artikelbezeichnung
     * @param bestand Anfangsbestand in Einzelstücken
     * @param preis Preis pro Stück
     * @param packungsgroesse Anzahl Stücke pro Verkaufseinheit
     */
    public Massengutartikel(int artikelnummer, String bezeichnung, int bestand, double preis, int packungsgroesse) {
        super(artikelnummer, bezeichnung, bestand, preis);
        this.packungsgroesse = packungsgroesse;
    }

    public int getPackungsgroesse() {
        return packungsgroesse;
    }

    /**
     * Prüft, ob eine Menge ein gültiges Vielfaches der Packungsgröße ist
     * @param menge die gewünschte Menge
     * @return true, wenn gültig
     */
    public boolean istGueltigeMenge(int menge) {
        return menge % packungsgroesse == 0;
    }

    @Override
    public String toString() {
        return super.toString() + " (Packungsgröße: " + packungsgroesse + ")";
    }
}
