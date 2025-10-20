package common.exceptions;

public class UngueltigeArtikelnummerException extends Exception {
    public UngueltigeArtikelnummerException() {
        super("Artikelnummer muss positiv sein.");
    }
}