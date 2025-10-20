package client;

import client.OnlineShopFassade;
import common.model.Mitarbeiter;

import javax.swing.*;
import java.awt.*;

/**
 * Klasse zum Alegen von Artikel, wenn man Benutzer ist
 */

public class ArtikelAnlegenPanel extends JPanel {

    public ArtikelAnlegenPanel(OnlineShopFassade fassade, Mitarbeiter mitarbeiter, ShopClientGUI gui) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));

        // Titel
        JLabel title = new JLabel("Neuen Artikel anlegen", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        // Wrapper zur vertikalen Zentrierung
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridBagLayout());
        centerWrapper.add(form);
        add(centerWrapper, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nummerField  = new JTextField(15);
        JTextField bezField     = new JTextField(15);
        JTextField preisField   = new JTextField(15);
        JTextField bestField    = new JTextField(15);
        JTextField masseField   = new JTextField(15);

        // Schriftgröße erhöhen
        Font feldFont = new Font("SansSerif", Font.PLAIN, 16);
        nummerField.setFont(feldFont);
        bezField.setFont(feldFont);
        preisField.setFont(feldFont);
        bestField.setFont(feldFont);
        masseField.setFont(feldFont);

        addRow(form, gbc, 0, "Artikelnummer:", nummerField);
        addRow(form, gbc, 1, "Bezeichnung:", bezField);
        addRow(form, gbc, 2, "Preis:", preisField);
        addRow(form, gbc, 3, "Bestand:", bestField);
        addRow(form, gbc, 4, "Packungsgröße (Massengutartikel):", masseField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton anlegenBtn = new JButton("Anlegen");
        JButton zurueckBtn = new JButton("Zurück");

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 16);
        anlegenBtn.setFont(buttonFont);
        zurueckBtn.setFont(buttonFont);

        btnPanel.add(anlegenBtn);
        btnPanel.add(zurueckBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ActionListener für Buttons
        anlegenBtn.addActionListener(e -> {
            try {
                int nummer   = Integer.parseInt(nummerField.getText().trim());
                String bez   = bezField.getText().trim();
                double preis = Double.parseDouble(preisField.getText().trim());
                int bestand  = Integer.parseInt(bestField.getText().trim());

                if (!masseField.getText().trim().isEmpty()) {
                    int packung = Integer.parseInt(masseField.getText().trim());
                    fassade.massengutArtikelAnlegen(nummer, bez, bestand, preis, packung, mitarbeiter);
                } else {
                    fassade.artikelAnlegen(nummer, bez, bestand, preis, mitarbeiter);
                }

                JOptionPane.showMessageDialog(this, "Artikel erfolgreich angelegt.");
                nummerField.setText(""); bezField.setText(""); preisField.setText("");
                bestField.setText(""); masseField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bitte gültige Zahlen eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        zurueckBtn.addActionListener(e -> {
            gui.setPanel("MitarbeiterMenü", new MitarbeiterPanel(fassade, mitarbeiter, gui));
            gui.zeigePanel("MitarbeiterMenü");
        });
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int y, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));

        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 0;
        p.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        p.add(field, gbc);
    }
}
