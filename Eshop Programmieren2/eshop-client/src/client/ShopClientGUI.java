package client;

import client.OnlineShopFassade;
import common.model.Benutzer;

import javax.swing.*;
import java.awt.*;

/**
 * Zentrale GUI Klasse
 * Verwaltet den Wechsel zwischen verschiedenen Panels
 * Steuert das Hauptlayout
 */

public class ShopClientGUI extends JFrame {

    private final OnlineShopFassade shop;  // geändert
    private Benutzer eingeloggterBenutzer;

    private final CardLayout card = new CardLayout();
    private final JPanel content = new JPanel(card);

    private final ShopMenuBar menuBar = new ShopMenuBar(this);

    public ShopClientGUI(OnlineShopFassade shop) {  // geändert
        super("eShop");
        this.shop = shop;

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        setJMenuBar(menuBar);

        content.add(new LoginPanel(shop, this), "Login");  // geändert
        zeigePanel("Login");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setPanel(String name, JPanel panel) {
        content.add(panel, name);
    }

    public void zeigePanel(String name) {
        card.show(content, name);
    }

    public OnlineShopFassade getFassade() {  // geändert
        return shop;
    }

    public Benutzer getEingeloggterBenutzer() {
        return eingeloggterBenutzer;
    }

    public void setEingeloggterBenutzer(Benutzer benutzer) {
        this.eingeloggterBenutzer = benutzer;
        menuBar.aktualisiereMenue();
    }

    public void setzePanel(JPanel neuerInhalt) {
        content.removeAll();
        content.add(neuerInhalt);
        content.revalidate();
        content.repaint();
    }
}

