package common.net;

import java.io.Serializable;

/**
 * Klasse repräsentiert die Response des Servers auf eine Request
 * Eine Response enthält Informationen darüber, ob die Anfrage erfolgreich war und Rückgabedatei
 */

public class Response implements Serializable {
    private final boolean success;
    private final Object data;
    private final String message;

    public Response(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

