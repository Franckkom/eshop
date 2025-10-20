package common.exceptions;

public class ArtikelExistiertBereitsException extends Exception {
    public ArtikelExistiertBereitsException() {
        super("Ein Artikel mit dieser Nummer existiert bereits.");
    }
}


