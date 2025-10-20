package logic;

import common.model.*;
import common.exceptions.*;
import common.net.Request;
import common.net.Response;
import common.net.Command;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Die zentrale Logik-Klasse für den eShop
 * Diese Klasse koordiniert die Common Klassen
 * Alle Änderungen und Abfragen erfolgen serverseitig über diese Fassade
 */

public class eShop implements Serializable {

    private Artikelverwaltung artikelverwaltung = new Artikelverwaltung();
    private Benutzerverwaltung benutzerverwaltung = new Benutzerverwaltung();
    private List<Ereignis> ereignisse = new ArrayList<>();
    private int idCounter = 1;
    private final Map<String, Kunde> eingeloggteKunden = new HashMap<>();

    public Artikelverwaltung getArtikelverwaltung() {
        return artikelverwaltung;
    }

    public List<Ereignis> getEreignisse() {
        return ereignisse;
    }

    public void setEreignisse(List<Ereignis> ereignisse) {
        this.ereignisse = ereignisse;
    }

    public List<Kunde> getKunden() {
        return benutzerverwaltung.getAlleKunden();
    }

    public void setKunden(List<Kunde> kunden) {
        benutzerverwaltung.setAlleKunden(kunden);
        updateIdCounter();
    }

    public List<Mitarbeiter> getMitarbeiter() {
        return benutzerverwaltung.getMitarbeiter();
    }

    public void setMitarbeiter(List<Mitarbeiter> mitarbeiter) {
        benutzerverwaltung.setAlleMitarbeiter(mitarbeiter);
        updateIdCounter();
    }

    private void updateIdCounter() {
        int max = 0;
        for (Benutzer b : benutzerverwaltung.getAlleBenutzer()) {
            if (b.getId() > max) {
                max = b.getId();
            }
        }
        idCounter = max + 1;
    }

    public void registriereKunde(String name, String land, String stadt, String plz, String strasse,
                                 String benutzername, String passwort) throws BenutzernameVergebenException {
        if (benutzerverwaltung.benutzerExistiert(benutzername)) {
            throw new BenutzernameVergebenException();
        }
        Kunde k = new Kunde(idCounter++, name, benutzername, passwort, strasse, plz, stadt, land);
        benutzerverwaltung.registriereKunde(k);
    }

    public void registriereMitarbeiter(String name, String benutzername, String passwort) throws BenutzernameVergebenException {
        if (benutzerverwaltung.benutzerExistiert(benutzername)) {
            throw new BenutzernameVergebenException();
        }
        Mitarbeiter m = new Mitarbeiter(idCounter++, name, benutzername, passwort);
        benutzerverwaltung.registriereMitarbeiter(m);
    }

    public Benutzer loggeBenutzerEin(String benutzername, String passwort) throws LoginFehlgeschlagenException {
        Benutzer b = benutzerverwaltung.login(benutzername, passwort);
        if (b == null) throw new LoginFehlgeschlagenException();

        // Kunde merken
        if (b instanceof Kunde kunde) {
            eingeloggteKunden.put(kunde.getBenutzername(), kunde);
        }

        return b;
    }

    public List<Artikel> zeigeAlleArtikel() {
        return artikelverwaltung.getAlleArtikel();
    }

    public void artikelAnlegen(int nummer, String bezeichnung, int bestand, double preis, Mitarbeiter mitarbeiter)
            throws ArtikelExistiertBereitsException, UngueltigeArtikelnummerException, UngueltigerBestandException {

        if (nummer <= 0) {
            throw new UngueltigeArtikelnummerException();
        }
        if (bestand < 0) {
            throw new UngueltigerBestandException("Bestand darf nicht negativ sein.");
        }
        if (preis < 0) {
            throw new UngueltigerBestandException("Preis darf nicht negativ sein.");
        }
        if (artikelverwaltung.findeArtikel(nummer) != null) {
            throw new ArtikelExistiertBereitsException();
        }

        Artikel a = new Artikel(nummer, bezeichnung, bestand, preis);
        artikelverwaltung.artikelAnlegen(a);
        ereignisse.add(new Ereignis(LocalDate.now().getDayOfYear(), a, bestand, "Mitarbeiter", mitarbeiter.getName(),"Neuanlage"));
    }

    public void bestandAendern(int artikelnummer, int neuerBestand, Mitarbeiter mitarbeiter) throws ArtikelNichtGefundenException, UngueltigerBestandException {
        Artikel a = artikelverwaltung.findeArtikel(artikelnummer);
        if (a == null) throw new ArtikelNichtGefundenException();
        if (neuerBestand < 0) throw new UngueltigerBestandException("Bestand darf nicht negativ sein.");
        a.setBestand(neuerBestand);
        ereignisse.add(new Ereignis(LocalDate.now().getDayOfYear(), a, neuerBestand, "Mitarbeiter", mitarbeiter.getName(),"Bestandsänderung"));
    }
    /**
     * Fügt einen Artikel in angegebener Menge zum Warenkorb von einem eingeloggten Kunden hinzu
     */


    public void artikelZumWarenkorb(Kunde kunde, int artikelnummer, int menge)
            throws ArtikelNichtGefundenException, MassengutException, UngueltigerBestandException {

        Kunde echterKunde = eingeloggteKunden.get(kunde.getBenutzername());
        if (echterKunde == null) throw new RuntimeException("Kunde nicht eingeloggt.");

        Artikel artikel = artikelverwaltung.findeArtikel(artikelnummer);
        if (artikel == null) throw new ArtikelNichtGefundenException();

        if (menge <= 0) throw new UngueltigerBestandException("Menge muss größer als 0 sein.");

        if (artikel instanceof Massengutartikel m) {
            if (!m.istGueltigeMenge(menge)) {
                throw new MassengutException("Nur Vielfache von " + m.getPackungsgroesse() + " erlaubt.");
            }
        }

        if (artikel.getBestand() < menge) {
            throw new UngueltigerBestandException("Nur noch " + artikel.getBestand() + " Stück verfügbar.");
        }

        echterKunde.getWarenkorb().artikelHinzufuegen(artikel, menge);
    }


    public Map<Artikel, Integer> warenkorbInhalt(Kunde kunde) {
        Kunde echterKunde = eingeloggteKunden.get(kunde.getBenutzername());
        if (echterKunde == null) throw new RuntimeException("Kunde nicht eingeloggt.");
        return echterKunde.getWarenkorb().getArtikelMap();
    }


    public double berechneWarenkorbSumme(Kunde kunde) {
        return kunde.getWarenkorb().berechneGesamtpreis();
    }

    public void warenkorbMengeAendern(Kunde kunde, int artikelnummer, int neueMenge)
            throws ArtikelNichtGefundenException {

        Kunde echterKunde = eingeloggteKunden.get(kunde.getBenutzername());
        if (echterKunde == null) throw new RuntimeException("Kunde nicht eingeloggt.");

        Artikel artikel = artikelverwaltung.findeArtikel(artikelnummer);
        if (artikel == null || !echterKunde.getWarenkorb().getArtikelMap().containsKey(artikel)) {
            throw new ArtikelNichtGefundenException();
        }

        if (neueMenge <= 0) {
            echterKunde.getWarenkorb().artikelEntfernen(artikel);
        } else {
            echterKunde.getWarenkorb().getArtikelMap().put(artikel, neueMenge);
        }
    }


    public void warenkorbLeeren(Kunde kunde) {
        Kunde echterKunde = eingeloggteKunden.get(kunde.getBenutzername());
        if (echterKunde != null) {
            echterKunde.getWarenkorb().leeren();
        }
    }


    /**
     * Führt den Kaufvorgang von einen Kunden durch
     * Alle Artikel im Warenkorb werden gekauft, der Bestand wird reduziert und eine Rechnung wird erstellt und angezeigt
     */

    public List<String> kaufAbschliessen(Kunde kunde) {
        Kunde echterKunde = eingeloggteKunden.get(kunde.getBenutzername());
        if (echterKunde == null) throw new RuntimeException("Kunde nicht eingeloggt.");

        List<String> rechnung = new ArrayList<>();
        Map<Artikel, Integer> map = echterKunde.getWarenkorb().getArtikelMap();

        if (map.isEmpty()) {
            rechnung.add("Warenkorb ist leer.");
            return rechnung;
        }

        int tag = LocalDate.now().getDayOfYear();
        rechnung.add("Kaufdatum: Tag " + tag);

        double gesamt = 0;
        for (Map.Entry<Artikel, Integer> eintrag : map.entrySet()) {
            Artikel a = eintrag.getKey();
            int menge = eintrag.getValue();
            double einzel = a.getPreis() * menge;

            rechnung.add(a.getBezeichnung() + " x " + menge + " = " + einzel + " €");
            a.setBestand(a.getBestand() - menge);
            gesamt += einzel;

            ereignisse.add(new Ereignis(tag, a, menge, "Kunde", echterKunde.getName(), "Verkauf"));
        }

        rechnung.add("Gesamt: " + gesamt + " €");
        echterKunde.getWarenkorb().leeren();

        return rechnung;
    }



    public void massengutArtikelAnlegen(int nummer, String bezeichnung, int bestand, double preis, int packungsgroesse, Mitarbeiter mitarbeiter)
            throws ArtikelExistiertBereitsException, UngueltigeArtikelnummerException, UngueltigerBestandException {

        if (nummer <= 0) {
            throw new UngueltigeArtikelnummerException();
        }
        if (bestand < 0) {
            throw new UngueltigerBestandException("Bestand darf nicht negativ sein.");
        }
        if (preis < 0) {
            throw new UngueltigerBestandException("Preis darf nicht negativ sein.");
        }
        if (packungsgroesse <= 0) {
            throw new UngueltigerBestandException("Packungsgröße muss positiv sein.");
        }
        if (artikelverwaltung.findeArtikel(nummer) != null) {
            throw new ArtikelExistiertBereitsException();
        }

        Massengutartikel a = new Massengutartikel(nummer, bezeichnung, bestand, preis, packungsgroesse);
        artikelverwaltung.artikelAnlegen(a);
        ereignisse.add(new Ereignis(LocalDate.now().getDayOfYear(), a, bestand, "Mitarbeiter", mitarbeiter.getName(),"Massengut Bestand"));
    }


    /**
     * Erzeugt eine Bestandshistorie der letzten 30 Tage für einen Artikel.
     * Die Historie zeigt pro Tag den Bestand
     */

    public List<String> getBestandshistorie(int artikelnummer)
            throws ArtikelNichtGefundenException, KeineEreignisseFuerArtikelException {

        Artikel artikel = artikelverwaltung.findeArtikel(artikelnummer);
        if (artikel == null) {
            throw new ArtikelNichtGefundenException();
        }

        int heute = LocalDate.now().getDayOfYear();
        int bestandHeute = artikel.getBestand();

        List<Ereignis> artikelEreignisse = ereignisse.stream()
                .filter(e -> e.getArtikel().getArtikelnummer() == artikelnummer)
                .toList();

        if (artikelEreignisse.isEmpty()) {
            throw new KeineEreignisseFuerArtikelException();
        }

        int ersterTag = artikelEreignisse.stream()
                .mapToInt(Ereignis::getTagDesJahres)
                .min()
                .orElse(heute);

        int startTag = Math.max(ersterTag, heute - 29);

        Map<Integer, Integer> deltaMap = new HashMap<>();
        for (Ereignis e : artikelEreignisse) {
            int tag = e.getTagDesJahres();
            int delta = e.getBenutzerTyp().equals("Kunde") ? e.getMenge() : -e.getMenge();
            deltaMap.put(tag, deltaMap.getOrDefault(tag, 0) + delta);
        }

        Map<Integer, Integer> bestandProTag = new LinkedHashMap<>();
        int bestand = bestandHeute;

        for (int tag = heute; tag >= startTag; tag--) {
            bestandProTag.put(tag, bestand);
            if (deltaMap.containsKey(tag)) {
                bestand += deltaMap.get(tag);
            }
        }

        List<String> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : bestandProTag.entrySet()) {
            result.add("Tag " + entry.getKey() + ": " + entry.getValue() + " Stück");
        }
        return result;
    }


    /**
     * Liefert die Entwicklung des Bestands von einem Artikel der letzten 30 Tage als Liste von Integer Werten
     */

    public List<Integer> getBestandswerte(int artikelnummer) throws ArtikelNichtGefundenException {
        Artikel artikel = artikelverwaltung.findeArtikel(artikelnummer);
        if (artikel == null) throw new ArtikelNichtGefundenException();

        int heute = LocalDate.now().getDayOfYear();
        int startTag = heute - 29;
        if (startTag < 1) startTag = 1;

        List<Ereignis> artikelEreignisse = ereignisse.stream()
                .filter(e -> e.getArtikel().getArtikelnummer() == artikelnummer)
                .sorted(Comparator.comparingInt(Ereignis::getTagDesJahres))
                .toList();

        Map<Integer, Integer> bestandProTag = new TreeMap<>();

        int bestand = 0;
        for (int tag = startTag; tag <= heute; tag++) {
            for (Ereignis e : artikelEreignisse) {
                if (e.getTagDesJahres() == tag) {
                    switch (e.getEreignistyp().toUpperCase()) {
                        case "VERKAUF" -> bestand -= e.getMenge();
                        case "EINLAGERUNG", "NEUANLAGE", "MASSENGUT BESTAND" -> bestand += e.getMenge();
                        case "BESTANDSÄNDERUNG" -> bestand = e.getMenge(); // Setzt den Bestand exakt
                    }
                }
            }
            bestandProTag.put(tag, bestand);
        }

        // Fülle die Liste mit genau 30 Tagen
        List<Integer> result = new ArrayList<>();
        for (int tag = heute - 29; tag <= heute; tag++) {
            result.add(bestandProTag.getOrDefault(tag, result.isEmpty() ? 0 : result.get(result.size() - 1)));
        }

        return result;
    }


    /**
     * Verarbeitet einen vom Client gesendeten Request und führt die entsprechende Aktion serverseitig aus
     * Die dispatch Methode interpretiert den im Request enthaltenen Command und ruft dann die passende Fachlogik auf
     */

    public Response dispatch(Request request) {
        try {
            Command cmd = request.getCommand();
            Object data = request.getPayload();

            switch (cmd) {

                case LOGIN -> {
                    Object[] d = (Object[]) data;
                    return new Response(true, loggeBenutzerEin((String) d[0], (String) d[1]), null);
                }

                case REGISTRIERE_KUNDE -> {
                    Object[] d = (Object[]) data;
                    registriereKunde((String) d[0], (String) d[1], (String) d[2], (String) d[3],
                            (String) d[4], (String) d[5], (String) d[6]);
                    return new Response(true, null, null);
                }

                case REGISTRIERE_MITARBEITER -> {
                    Object[] d = (Object[]) data;
                    registriereMitarbeiter((String) d[0], (String) d[1], (String) d[2]);
                    return new Response(true, null, null);
                }

                case ALLE_ARTIKEL -> {
                    return new Response(true, zeigeAlleArtikel(), null);
                }

                case ARTIKEL_ANLEGEN -> {
                    Object[] d = (Object[]) data;
                    artikelAnlegen((int) d[0], (String) d[1], (int) d[2], (double) d[3], (Mitarbeiter) d[4]);
                    return new Response(true, null, null);
                }

                case MASSENGUT_ANLEGEN -> {
                    Object[] d = (Object[]) data;
                    massengutArtikelAnlegen((int) d[0], (String) d[1], (int) d[2], (double) d[3],
                            (int) d[4], (Mitarbeiter) d[5]);
                    return new Response(true, null, null);
                }

                case BESTAND_AENDERN -> {
                    Object[] d = (Object[]) data;
                    bestandAendern((int) d[0], (int) d[1], (Mitarbeiter) d[2]);
                    return new Response(true, null, null);
                }

                case WARENKORB_HINZUFUEGEN -> {
                    Object[] d = (Object[]) data;
                    artikelZumWarenkorb((Kunde) d[0], (int) d[1], (int) d[2]);
                    return new Response(true, null, null);
                }


                case WARENKORB_MENGE_AENDERN -> {
                    Object[] d = (Object[]) data;
                    warenkorbMengeAendern((Kunde) d[0], (int) d[1], (int) d[2]);
                    return new Response(true, null, null);
                }

                case WARENKORB_LEEREN -> {
                    warenkorbLeeren((Kunde) data);
                    return new Response(true, null, null);
                }

                case WARENKORB_INHALT -> {
                    return new Response(true, warenkorbInhalt((Kunde) data), null);
                }

                case WARENKORB_SUMME -> {
                    return new Response(true, berechneWarenkorbSumme((Kunde) data), null);
                }

                case KAUF_ABSCHLIESSEN -> {
                    return new Response(true, kaufAbschliessen((Kunde) data), null);
                }

                case BESTANDSHISTORIE -> {
                    return new Response(true, getBestandshistorie((int) data), null);
                }

                case BESTANDSWERTE -> {
                    return new Response(true, getBestandswerte((int) data), null);
                }

                case EREIGNISSE -> {
                    return new Response(true, getEreignisse(), null);
                }

                case KUNDEN -> {
                    return new Response(true, getKunden(), null);
                }

                case MITARBEITER -> {
                    return new Response(true, getMitarbeiter(), null);
                }

                case QUIT -> {
                    return new Response(true, null, "Verbindung wird beendet.");
                }

                default -> {
                    return new Response(false, null, "Unbekannter Befehl: " + cmd);
                }
            }

        } catch (Exception e) {
            return new Response(false, null, "Fehler bei Verarbeitung: " + e.getMessage());
        }
    }
}

