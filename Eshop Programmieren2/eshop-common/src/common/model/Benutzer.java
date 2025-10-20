package common.model;
import java.io.Serializable;

/**
 * Diese Klasse wird als Basis für Kunden und Mitarbeiter verwendet, die dann davon erben.
 */

public class Benutzer implements Serializable{
    protected int id;                 // eindeutige ID des Benutzers
    protected String name;
    protected String benutzername;
    protected String passwort;

    /**
     * Konstruktor: Erstellt einen neuen Benutzer
     * @param id die ID des Benutzers
     * @param name der Name des Benutzers
     * @param benutzername Benutzername zum Einloggen
     * @param passwort Passwort zum Einloggen
     */
    public Benutzer(int id, String name, String benutzername, String passwort) {
        this.id = id;
        this.name = name;
        this.benutzername = benutzername;
        this.passwort = passwort;
    }
    //Getter Methoden
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBenutzername() {
        return benutzername;
    }

    // checkt, ob das eingegebene Passwort korrekt ist von der Eingabe bei der Registrierung
    public boolean checkPasswort(String passwort) {
        return this.passwort.equals(passwort);
    }
}
