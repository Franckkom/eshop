package common.exceptions;

public class BenutzernameVergebenException extends Exception {
    public BenutzernameVergebenException() {
        super("Der Benutzername ist bereits vergeben.");
    }
}