package common.net;

/**
 * Dieses Enum definiert alle möglichen Kommandos, die ein Client an den Server senden kann
 * Die Kommandos werden verwendet, um zwischen verschiedenen Anfragen im Netzwerk zu unterscheiden
 */

public enum Command {
    LOGIN,
    REGISTRIERE_KUNDE,
    REGISTRIERE_MITARBEITER,
    ALLE_ARTIKEL,
    ARTIKEL_ANLEGEN,
    MASSENGUT_ANLEGEN,
    BESTAND_AENDERN,
    WARENKORB_HINZUFUEGEN,
    WARENKORB_MENGE_AENDERN,
    WARENKORB_LEEREN,
    WARENKORB_INHALT,
    WARENKORB_SUMME,
    KAUF_ABSCHLIESSEN,
    BESTANDSHISTORIE,
    BESTANDSWERTE,
    EREIGNISSE,
    KUNDEN,
    MITARBEITER,
    QUIT
}

