package logic;

import common.model.Artikel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse verwaltet alle Artikel im System
 * Bietet verschiedene Methoden an
 */

public class Artikelverwaltung implements Serializable {
    private List<Artikel> artikelListe = new ArrayList<>();


    public void artikelAnlegen(Artikel artikel) {
        artikelListe.add(artikel);
    }


    public Artikel findeArtikel(int artikelnummer) {
        for (Artikel a : artikelListe) {
            if (a.getArtikelnummer() == artikelnummer) {
                return a;
            }
        }
        return null;
    }

    public List<Artikel> getAlleArtikel() {
        return artikelListe;
    }


    public void setAlleArtikel(List<Artikel> artikelListe) {
        this.artikelListe = artikelListe;
    }
}
