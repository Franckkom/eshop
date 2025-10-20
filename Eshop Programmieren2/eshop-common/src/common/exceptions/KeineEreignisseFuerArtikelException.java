package common.exceptions;

public class KeineEreignisseFuerArtikelException extends Exception {
    public KeineEreignisseFuerArtikelException() {
        super("Es sind keine Ereignisse für diesen Artikel vorhanden.");
    }
}