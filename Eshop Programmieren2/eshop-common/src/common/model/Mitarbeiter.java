package common.model;
import java.io.Serializable;

/**
 * Mitarbeiter erbt von Klasse Benutzer
 */
public class Mitarbeiter extends Benutzer implements Serializable{


    public Mitarbeiter(int id, String name, String benutzername, String passwort) {
        super(id, name, benutzername, passwort);
    }
}

