package common.exceptions;

public class ArtikelNichtGefundenException extends Exception {
    public ArtikelNichtGefundenException() {
        super("Der Artikel wurde nicht gefunden.");
    }
}