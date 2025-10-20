package logic;

import common.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse verwaltet alle Benutzer im System
 * Bietet verschiedene Methoden an
 */


public class Benutzerverwaltung implements Serializable {
    private List<Benutzer> benutzerListe = new ArrayList<>();


    public void registriereKunde(Kunde kunde) {
        benutzerListe.add(kunde);
    }


    public void registriereMitarbeiter(Mitarbeiter mitarbeiter) {
        benutzerListe.add(mitarbeiter);
    }


    public Benutzer login(String benutzername, String passwort) {
        for (Benutzer b : benutzerListe) {
            if (b.getBenutzername() != null && b.checkPasswort(passwort)) {
                if (b.getBenutzername().equals(benutzername)) {
                    return b;
                }
            }
        }
        return null;
    }

    public boolean benutzernameVergeben(String benutzername) {
        for (Benutzer b : benutzerListe) {
            if (b.getBenutzername().equals(benutzername)) {
                return true;
            }
        }
        return false;
    }

    public boolean benutzerExistiert(String benutzername) {
        for (Benutzer b : benutzerListe) {
            if (b.getBenutzername() != null && b.getBenutzername().equalsIgnoreCase(benutzername)) {
                return true;
            }
        }
        return false;
    }

    public List<Kunde> getAlleKunden() {
        List<Kunde> kunden = new ArrayList<>();
        for (Benutzer b : benutzerListe) {
            if (b instanceof Kunde) {
                kunden.add((Kunde) b);
            }
        }
        return kunden;
    }

    public List<Mitarbeiter> getAlleMitarbeiter() {
        List<Mitarbeiter> mitarbeiter = new ArrayList<>();
        for (Benutzer b : benutzerListe) {
            if (b instanceof Mitarbeiter) {
                mitarbeiter.add((Mitarbeiter) b);
            }
        }
        return mitarbeiter;
    }

    public void setAlleKunden(List<Kunde> kunden) {
        benutzerListe.removeIf(b -> b instanceof Kunde);
        benutzerListe.addAll(kunden);
    }

    public void setAlleMitarbeiter(List<Mitarbeiter> mitarbeiter) {
        benutzerListe.removeIf(b -> b instanceof Mitarbeiter);
        benutzerListe.addAll(mitarbeiter);
    }

    public List<Benutzer> getAlleBenutzer() {
        return benutzerListe;
    }

    public List<Mitarbeiter> getMitarbeiter() {
        return getAlleMitarbeiter();
    }
}

