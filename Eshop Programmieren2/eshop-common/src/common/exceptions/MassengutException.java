package common.exceptions;

public class MassengutException extends Exception {
    public MassengutException() {
        super("Die Menge muss ein Vielfaches der Packungsgröße sein.");
    }

    public MassengutException(String message) {
        super(message);
    }
}
