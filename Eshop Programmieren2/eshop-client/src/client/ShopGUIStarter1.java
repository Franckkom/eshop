package client;

import javax.swing.*;

/**
 * 1. Startklasse für den eShop
 * Testet die Verbindung zum Server
 */

public class ShopGUIStarter1 {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                OnlineShopFassade shop = new OnlineShopFassade("localhost", 12345);


                if (!shop.testverbindung()) {
                    JOptionPane.showMessageDialog(null,
                            "Keine Verbindung zum Server. Stelle sicher, dass der Server läuft.",
                            "Verbindungsfehler", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }

                ShopClientGUI gui = new ShopClientGUI(shop);
                gui.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Fehler beim Start:\n" + ex.getMessage(),
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}

