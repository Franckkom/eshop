# Eshop (Uni-Projekt)
**Tech:** Java (IntelliJ-Projekt)

## Start (einfach)
1. In **IntelliJ** öffnen (Ordner importieren).
2. Projekt-SDK **Java 17/21** wählen.
3. Server starten: `Eshop Programmieren2/eshop-server/src` → Klasse mit `main`.
4. (Optional) Client starten, falls vorhanden.

## Start (Terminal, Beispiel)
find "Eshop Programmieren2" -name "*.java" > sources.txt
javac @sources.txt -d out
# Ersetze <MainKlasse> (z. B. server.OnlineShopServer)
java -cp out <MainKlasse>
