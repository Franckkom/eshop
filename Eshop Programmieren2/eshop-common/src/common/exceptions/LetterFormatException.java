package common.exceptions;

public class LetterFormatException extends Exception {
    public LetterFormatException() {
        super("Nur Buchstaben dürfen eingeben werden.");
    }
}