package client;

import client.OnlineShopFassade;
import common.model.Kunde;
import common.model.Mitarbeiter;

import javax.swing.*;

/**
 * Menüleiste für den eShop
 * Passt die verfügbaren Menüeinträge an, basierend auf Art des Benutzers also entweder Mitarbeiter oder Kunde
 */

public class ShopMenuBar extends JMenuBar {

    private final ShopClientGUI gui;

    public ShopMenuBar(ShopClientGUI gui) {
        this.gui = gui;
        baueMenue();
    }

    public void aktualisiereMenue() {
        removeAll();
        baueMenue();
        revalidate();
        repaint();
    }

    private void baueMenue() {
        JMenu menue = new JMenu("Menü");
        Object benutzer = gui.getEingeloggterBenutzer();
        OnlineShopFassade shop = gui.getFassade();

        if (benutzer instanceof Kunde) {
            menue.add(menuItem("Artikel anzeigen", "Artikel",
                    () -> new ArtikelPanel(gui)));
            menue.add(menuItem("Warenkorb", "Warenkorb",
                    () -> new WarenkorbPanel(gui)));
        }

        if (benutzer instanceof Mitarbeiter mitarbeiter) {
            menue.add(menuItem("Artikel anzeigen", "Artikel",
                    () -> new ArtikelPanel(gui)));
            menue.add(menuItem("Artikel anlegen", "ArtikelAnlegen",
                    () -> new ArtikelAnlegenPanel(shop, mitarbeiter, gui)));
        }

        if (benutzer != null) {
            menue.addSeparator();
            JMenuItem logout = new JMenuItem("Logout");
            logout.addActionListener(e -> {
                gui.setEingeloggterBenutzer(null);
                gui.setPanel("Login", new LoginPanel(shop, gui));
                gui.zeigePanel("Login");
            });
            menue.add(logout);
        }

        add(menue);
    }

    private JMenuItem menuItem(String titel, String panelName, java.util.function.Supplier<JPanel> supplier) {
        JMenuItem item = new JMenuItem(titel);
        item.addActionListener(e -> {
            gui.setPanel(panelName, supplier.get());
            gui.zeigePanel(panelName);
        });
        return item;
    }
}
