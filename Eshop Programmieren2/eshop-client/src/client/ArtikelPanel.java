package client;

import common.model.Artikel;
import common.model.Benutzer;
import common.model.Kunde;
import common.model.Mitarbeiter;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * Zeigt bestehende Artikel an
 * Dieses Panel passt sich dem Benutzer je nach Benutzer an
 * Beim Kunden kann man Artikel in Warenkorb legen
 * Beim Mitarbeiter kann man Bestand ändern
 */
public class ArtikelPanel extends JPanel {

    private final ShopClientGUI gui;
    private final OnlineShopFassade fassade;
    private final JComboBox<String> sortBox;

    public ArtikelPanel(ShopClientGUI gui) {
        this.gui = gui;
        this.fassade = gui.getFassade();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel header = new JLabel("Artikelübersicht", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listPanel);
        add(scroll, BorderLayout.CENTER);

        String[] sortierungen = {
                "Artikelnummer (aufsteigend)", "Artikelnummer (absteigend)",
                "Bezeichnung (A-Z)", "Bezeichnung (Z-A)",
                "Preis (aufsteigend)", "Preis (absteigend)"
        };
        sortBox = new JComboBox<>(sortierungen);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton refresh = new JButton("Aktualisieren");
        bottom.add(sortBox);
        bottom.add(refresh);

        if (gui.getEingeloggterBenutzer() instanceof Mitarbeiter m) {
            JButton back = new JButton("Zurück");
            back.addActionListener(e -> {
                gui.setPanel("MitarbeiterMenü", new MitarbeiterPanel(fassade, m, gui));
                gui.zeigePanel("MitarbeiterMenü");
            });
            bottom.add(back);
        }

        add(bottom, BorderLayout.SOUTH);
        refresh.addActionListener(e -> ladeArtikel(listPanel));
        ladeArtikel(listPanel);
    }

    /**
     *
     * @param listPanel
     */
    private void ladeArtikel(JPanel listPanel) {
        listPanel.removeAll();
        Benutzer user = gui.getEingeloggterBenutzer();
        List<Artikel> alle = fassade.zeigeAlleArtikel();

        String auswahl = (String) sortBox.getSelectedItem();
        Comparator<Artikel> comp = switch (auswahl) {
            case "Artikelnummer (aufsteigend)" -> Comparator.comparingInt(Artikel::getArtikelnummer);
            case "Artikelnummer (absteigend)" -> Comparator.comparingInt(Artikel::getArtikelnummer).reversed();
            case "Bezeichnung (von A bis Z)" -> Comparator.comparing(Artikel::getBezeichnung, String.CASE_INSENSITIVE_ORDER);
            case "Bezeichnung (von Z bis A)" -> Comparator.comparing(Artikel::getBezeichnung, String.CASE_INSENSITIVE_ORDER).reversed();
            case "Preis (aufsteigend)" -> Comparator.comparingDouble(Artikel::getPreis);
            case "Preis (absteigend)" -> Comparator.comparingDouble(Artikel::getPreis).reversed();
            default -> null;
        };
        if (comp != null) alle.sort(comp);

        for (Artikel a : alle) {
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            box.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel title = new JLabel(a.getArtikelnummer() + " - " + a.getBezeichnung(), SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 15));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(title);
            box.add(Box.createVerticalStrut(8));

            String infoText = "Preis: " + String.format("%.2f €", a.getPreis());
            if (user instanceof Mitarbeiter) {
                infoText = "Bestand: " + a.getBestand() + " | " + infoText;
            }

            JLabel info = new JLabel(infoText);
            info.setFont(new Font("SansSerif", Font.PLAIN, 13));
            info.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(info);
            box.add(Box.createVerticalStrut(10));

            JButton btn;
            //Kunde
            if (user instanceof Kunde kunde) {
                btn = new JButton("In den Warenkorb");
                btn.addActionListener(e -> {
                    String mengeStr = JOptionPane.showInputDialog(this, "Menge:");
                    if (mengeStr != null) {
                        try {
                            int menge = Integer.parseInt(mengeStr);
                            fassade.artikelZumWarenkorb(kunde, a.getArtikelnummer(), menge);
                            JOptionPane.showMessageDialog(this, "Artikel erfolgreich hinzugefügt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                            gui.setPanel("Warenkorb", new WarenkorbPanel(gui));
                            gui.zeigePanel("Warenkorb");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
               //Mitarbeiter
            } else if (user instanceof Mitarbeiter mitarbeiter) {
                btn = new JButton("Bestand ändern");
                btn.addActionListener(e -> {
                    String neuStr = JOptionPane.showInputDialog(this, "Neuer Bestand:");
                    if (neuStr != null) {
                        try {
                            int neu = Integer.parseInt(neuStr);
                            fassade.bestandAendern(a.getArtikelnummer(), neu, mitarbeiter);
                            ladeArtikel(listPanel);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage());
                        }
                    }
                });
            } else {
                btn = new JButton("Keine Aktion");
                btn.setEnabled(false);
            }

            JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            btnWrapper.setOpaque(false);
            btnWrapper.add(btn);
            box.add(btnWrapper);

            listPanel.add(box);
            listPanel.add(Box.createVerticalStrut(15));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}


