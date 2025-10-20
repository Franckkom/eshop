package common.model;

import java.io.Serializable;

/**
 * Ein Ereignis zeigt, wann ein Artikel eingelagert oder verkauft wurde, was der Mitarbeiter einsehen kann.
 */
public class Ereignis implements Serializable {
    private static final long serialVersionUID = 1L;
    private int tagDesJahres;
    private Artikel artikel;
    private int menge;
    private String benutzerTyp;
    private String benutzerName;
    private String ereignistyp;

    public Ereignis(int tagDesJahres, Artikel artikel, int menge, String benutzerTyp, String benutzerName, String ereignistyp) {
        this.tagDesJahres = tagDesJahres;
        this.artikel = artikel;
        this.menge = menge;
        this.benutzerTyp = benutzerTyp;
        this.benutzerName = benutzerName;
        this.ereignistyp = ereignistyp;
    }

    public int getTag() {
        return tagDesJahres;
    }

    public int getTagDesJahres() {
        return tagDesJahres;
    }

    public Artikel getArtikel() {
        return artikel;
    }

    public int getMenge() {
        return menge;
    }

    public String getBenutzerTyp() {
        return benutzerTyp;
    }

    public String getBenutzerName() {
        return benutzerName;
    }

    public String getEreignistyp() {
        return ereignistyp;
    }

    public boolean istEinlagerung() {
        return "EINLAGERUNG".equalsIgnoreCase(ereignistyp);
    }

    public boolean istVerkauf() {
        return "VERKAUF".equalsIgnoreCase(ereignistyp);
    }

    public boolean istKorrektur() {
        return "KORREKTUR".equalsIgnoreCase(ereignistyp);
    }

    /**
     * Gibt das Ereignis als Text zurück (für Anzeige im Protokoll für den Mitarbeiter)
     */
    @Override
    public String toString() {
        return "[" + tagDesJahres + "] "
                + benutzerTyp + " " + benutzerName + " → "
                + artikel.getBezeichnung() + " (" + menge + ")"
                + " [" + ereignistyp + "]";
    }
}

