package client;

import common.model.Artikel;
import common.model.Benutzer;
import common.model.Kunde;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel zur Darstellung und Verwaltung des Warenkorbs eines Kunden.
 * Zeigt alle Artikel mit Mengen und Gesamtpreis und ermöglicht Kauf oder Leeren des Inhalts von dem Warenkorb
 */

public class WarenkorbPanel extends JPanel {

    private final ShopClientGUI gui;
    private final OnlineShopFassade shop;
    private final JTextArea inhaltArea;

    public WarenkorbPanel(ShopClientGUI gui) {
        this.gui = gui;
        this.shop = gui.getFassade();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titel = new JLabel("Warenkorb", SwingConstants.CENTER);
        titel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titel, BorderLayout.NORTH);

        inhaltArea = new JTextArea();
        inhaltArea.setEditable(false);
        inhaltArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        inhaltArea.setMargin(new Insets(15, 20, 15, 20));
        inhaltArea.setBackground(new Color(248, 248, 248));
        inhaltArea.setLineWrap(true);
        inhaltArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(inhaltArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton kaufen = new JButton("Kaufen");
        JButton leeren = new JButton("Leeren");
        JButton aktualisieren = new JButton("Aktualisieren");

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 16);
        kaufen.setFont(buttonFont);
        leeren.setFont(buttonFont);
        aktualisieren.setFont(buttonFont);

        buttonPanel.add(kaufen);
        buttonPanel.add(leeren);
        buttonPanel.add(aktualisieren);
        add(buttonPanel, BorderLayout.SOUTH);

        kaufen.addActionListener(e -> kaufAbschliessen());
        leeren.addActionListener(e -> warenkorbLeeren());
        aktualisieren.addActionListener(e -> aktualisiereWarenkorb());

        aktualisiereWarenkorb();
    }
    private void aktualisiereWarenkorb() {
        inhaltArea.setText("");

        Benutzer benutzer = gui.getEingeloggterBenutzer();
        if (!(benutzer instanceof Kunde kunde)) {
            inhaltArea.setText("Kein Kunde eingeloggt.");
            return;
        }

        try {
            Map<Artikel, Integer> artikelMap = shop.warenkorbInhalt(kunde);

            if (artikelMap == null || artikelMap.isEmpty()) {
                inhaltArea.setText("Der Warenkorb ist leer.");
                return;
            }

            double gesamt = 0;
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<Artikel, Integer> eintrag : artikelMap.entrySet()) {
                Artikel artikel = eintrag.getKey();
                int menge = eintrag.getValue();
                double preis = artikel.getPreis() * menge;

                sb.append(String.format("%s  x %2d  =  %7.2f €\n", artikel.getBezeichnung(), menge, preis));
                gesamt += preis;
            }

            sb.append("\n");
            sb.append(String.format("Gesamtbetrag: %.2f €", gesamt));

            inhaltArea.setText(sb.toString());

        } catch (Exception ex) {
            inhaltArea.setText("Fehler beim Abrufen des Warenkorbs: " + ex.getMessage());
        }
    }
//Artikel kauf
    private void kaufAbschliessen() {
        if (!(gui.getEingeloggterBenutzer() instanceof Kunde kunde)) return;

        try {
            List<String> rechnung = shop.kaufAbschliessen(kunde);
            JOptionPane.showMessageDialog(this, String.join("\n", rechnung), "Rechnung", JOptionPane.INFORMATION_MESSAGE);
            aktualisiereWarenkorb();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kauf fehlgeschlagen: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
//Warenkorb leeren
    private void warenkorbLeeren() {
        if (!(gui.getEingeloggterBenutzer() instanceof Kunde kunde)) return;

        try {
            shop.warenkorbLeeren(kunde);
            aktualisiereWarenkorb();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Leeren fehlgeschlagen: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
