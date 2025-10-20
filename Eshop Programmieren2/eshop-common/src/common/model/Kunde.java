package common.model;
import java.io.Serializable;

/**
 * Kunde erbt von Klasse Benutzer mit den extra Eigenschaften Adresse und Warenkorb
 */
public class Kunde extends Benutzer implements Serializable{

    private String strasse;
    private String plz;
    private String stadt;
    private String land;
    private Warenkorb warenkorb;

    /**
     * Konstruktor: Erstellt einen neuen Kunden
     * @param name Name des Kunden
     * @param benutzername Benutzername zum Einloggen
     * @param passwort Passwort zum Einloggen
     * @param strasse Straße und Hausnummer
     * @param plz Postleitzahl
     * @param stadt Wohnort
     * @param land Land
     */
    public Kunde(int id, String name, String benutzername, String passwort,
                 String strasse, String plz, String stadt, String land) {
        super(id, name, benutzername, passwort);  // ruft Konstruktor von Benutzer auf
        this.strasse = strasse;
        this.plz = plz;
        this.stadt = stadt;
        this.land = land;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getPlz() {
        return plz;
    }

    public String getStadt() {
        return stadt;
    }

    public String getLand() {
        return land;
    }


    public String getVollständigeAdresse() {
        return strasse + ", " + plz + " " + stadt + ", " + land;
    }


    public Warenkorb getWarenkorb() {
        if (warenkorb == null) {
            warenkorb = new Warenkorb();
        }
        return warenkorb;
    }
}
