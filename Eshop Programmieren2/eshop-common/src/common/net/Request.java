package common.net;

import java.io.Serializable;

public class Request implements Serializable {
    private final Command command;
    private final Object payload;

    /**
     * Die Request Klasse repräsentiert eine Nachricht vom Client an den Server
     * innerhalb des Client-Server-Kommunikationsprotokolls
     * Eine Anfrage besteht aus einem Command der angibt, was gemacht werden soll
     * und einem optionalen payload der zusätzliche Daten enthält
     */

    public Request(Command command, Object payload) {
        this.command = command;
        this.payload = payload;
    }

    public Command getCommand() {
        return command;
    }

    public Object getPayload() {
        return payload;
    }
}

