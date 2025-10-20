package client;

import client.OnlineShopFassade;
import common.model.Mitarbeiter;

import javax.swing.*;
import java.awt.*;

/**
 * Mitarbeiter Menü Panel, was alle Funktionen des Mitarbeiters anzeigt
 * Mitarbeiter registrieren
 */

public class MitarbeiterPanel extends JPanel {

    private final OnlineShopFassade shop;
    private final Mitarbeiter mitarbeiter;
    private final ShopClientGUI gui;

    public MitarbeiterPanel(OnlineShopFassade shop, Mitarbeiter mitarbeiter, ShopClientGUI gui) {
        this.shop = shop;
        this.mitarbeiter = mitarbeiter;
        this.gui = gui;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Willkommen, " + mitarbeiter.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton artikelAnzeigenBtn         = new JButton("Artikel anzeigen");
        JButton artikelAnlegenBtn          = new JButton("Artikel anlegen");
        JButton mitarbeiterRegistrierenBtn = new JButton("Mitarbeiter registrieren");
        JButton ereignisseBtn              = new JButton("Ereignisse anzeigen");
        JButton bestandshistorieBtn        = new JButton("Bestandshistorie anzeigen");

        buttonPanel.add(artikelAnzeigenBtn);
        buttonPanel.add(artikelAnlegenBtn);
        buttonPanel.add(mitarbeiterRegistrierenBtn);
        buttonPanel.add(ereignisseBtn);
        buttonPanel.add(bestandshistorieBtn);

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        artikelAnzeigenBtn.addActionListener(e ->
                gui.setzePanel(new ArtikelPanel(gui))
        );

        artikelAnlegenBtn.addActionListener(e ->
                gui.setzePanel(new ArtikelAnlegenPanel(shop, mitarbeiter, gui))
        );

//Mitarbeiter registrieren

        mitarbeiterRegistrierenBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField userField = new JTextField();
            JPasswordField pwField = new JPasswordField();

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Benutzername:"));
            panel.add(userField);
            panel.add(new JLabel("Passwort:"));
            panel.add(pwField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Mitarbeiter registrieren",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String benutzername = userField.getText().trim();
                String passwort = new String(pwField.getPassword()).trim();

                if (name.isEmpty() || benutzername.isEmpty() || passwort.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen.", "Fehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    shop.registriereMitarbeiter(name, benutzername, passwort);
                    JOptionPane.showMessageDialog(this, "Mitarbeiter registriert.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ereignisseBtn.addActionListener(e ->
                gui.setzePanel(new EreignisTabellePanel(shop, gui))
        );

        bestandshistorieBtn.addActionListener(e -> {
            String eingabe = JOptionPane.showInputDialog(this, "Artikelnummer eingeben:");
            if (eingabe != null && !eingabe.trim().isEmpty()) {
                try {
                    int artikelnummer = Integer.parseInt(eingabe.trim());
                    java.util.List<Integer> werte = shop.getBestandswerte(artikelnummer);
                    JFrame frame = new JFrame("Bestandshistorie Artikel " + artikelnummer);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setContentPane(new BestandshistoriePanel(werte));
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ungültige Artikelnummer!", "Fehler", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

