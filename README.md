# Eshop (Uni-Projekt, Java)

Mehrschichtige Java-Anwendung mit **Server**, **Client (Swing GUI)** und einem **Common**-Modul (Model/Net/Exceptions).
Ziel: kleiner Shop mit Artikeln, Kunden, Warenkorb und Ereignisprotokoll.

## Kurzüberblick
- **Server**: Geschäftslogik (Artikelverwaltung, Benutzerverwaltung, Requests/Responses)
- **Client**: Desktop-GUI (Swing) mit Panels (Login, Artikel, Warenkorb, etc.)
- **Common**: Domänenmodelle und einfache Protokolle (`common.model`, `common.net`, `common.exceptions`)
- **Persistenz/Demo-Daten**: Serialisierte Dateien (`artikel.ser`, `kunden.ser`, `mitarbeiter.ser`, `ereignisse.ser`)

## Ordnerstruktur (vereinfacht)

## Start (IntelliJ – empfohlen)
1. Projekt in **IntelliJ** öffnen (Ordner importieren).  
2. **Project SDK** auf **Java 17** (oder 21) setzen.  
3. In den Moduleinstellungen diese Ordner als *Sources* markieren:
   - `…/eshop-common/src`, `…/eshop-server/src`, `…/eshop-client/src`
4. **Run/Debug-Konfigurationen** anlegen:
   - **Server** – Main-Klasse: `server.OnlineShopServer`
   - **Client** – Main-Klasse: `client.ShopGUIStarter2`  
     *(falls nicht vorhanden/fehlschlägt → `client.ShopGUIStarter1`)*  
5. Erst **Server**, dann **Client** starten.

## Start (Terminal – ohne IDE)
> Kompiliert alle `.java` Dateien nach `out/` und startet dann Server/Client.
```bash
# im Repo-Root
find "Eshop Programmieren2" -name "*.java" > sources.txt
javac @sources.txt -d out

# Server starten (Terminal 1, offen lassen)
java -cp out server.OnlineShopServer

# Client starten (Terminal 2)
java -cp out client.ShopGUIStarter2
# Alternative (falls nötig):
# java -cp out client.ShopGUIStarter1
