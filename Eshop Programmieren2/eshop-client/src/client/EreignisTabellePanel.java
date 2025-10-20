package client;

import common.model.Ereignis;
import common.model.Artikel;
import common.model.Mitarbeiter;
import common.model.Benutzer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


/**
 * Klasse zum anzeigen der Ereignisse in einer Tabelle
 */

public class  EreignisTabellePanel extends JPanel {


    public EreignisTabellePanel(OnlineShopFassade fassade, ShopClientGUI gui) {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titel = new JLabel("Ereignisübersicht", SwingConstants.CENTER);
        titel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titel, BorderLayout.NORTH);

        String[] spalten = { "Tag", "Artikelnummer", "Bezeichnung", "Benutzertyp", "Person", "Ereignistyp" };

        List<Ereignis> ereignisse;
        try {
            ereignisse = fassade.getEreignisse();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Ereignisse: " + ex.getMessage(),
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            ereignisse = List.of();
        }

        String[][] daten = new String[ereignisse.size()][spalten.length];
        for (int i = 0; i < ereignisse.size(); i++) {
            Ereignis e = ereignisse.get(i);
            Artikel a = e.getArtikel();
            daten[i][0] = String.valueOf(e.getTagDesJahres());
            daten[i][1] = String.valueOf(a.getArtikelnummer());
            daten[i][2] = a.getBezeichnung();
            daten[i][3] = e.getBenutzerTyp();
            daten[i][4] = e.getBenutzerName();
            daten[i][5] = e.getEreignistyp();
        }

        DefaultTableModel model = new DefaultTableModel(daten, spalten) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JButton zurueck = new JButton("Zurück");
        zurueck.addActionListener(e -> {
            Benutzer benutzer = gui.getEingeloggterBenutzer();
            if (benutzer instanceof Mitarbeiter m) {
                gui.setzePanel(new MitarbeiterPanel(fassade, m, gui));
            } else {
                JOptionPane.showMessageDialog(this, "Unberechtigter Zugriff.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(zurueck);
        add(bottom, BorderLayout.SOUTH);
    }
}



