package client;

import client.OnlineShopFassade;
import common.exceptions.LetterFormatException;

import javax.swing.*;
import java.awt.*;

/**
 * Registrierungs-Panel zur Kundenregistrierung, aufrufbar im LoginPanel.
 */
public class RegistrierungPanel extends JPanel {

    public RegistrierungPanel(OnlineShopFassade shop, ShopClientGUI gui) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));  // mehr oben, weniger unten

        // Titel
        JLabel titel = new JLabel("Kundenregistrierung", SwingConstants.CENTER);
        titel.setFont(new Font("SansSerif", Font.BOLD, 26));
        add(titel, BorderLayout.NORTH);

        // Wrapper zur vertikalen Zentrierung
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridBagLayout());
        centerWrapper.add(form);
        add(centerWrapper, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Eingabefelder
        JTextField nameF    = new JTextField(15);
        JTextField landF    = new JTextField(15);
        JTextField stadtF   = new JTextField(15);
        JTextField plzF     = new JTextField(15);
        JTextField strasseF = new JTextField(15);
        JTextField userF    = new JTextField(15);
        JPasswordField pwF  = new JPasswordField(15);

        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);
        nameF.setFont(fieldFont);
        landF.setFont(fieldFont);
        stadtF.setFont(fieldFont);
        plzF.setFont(fieldFont);
        strasseF.setFont(fieldFont);
        userF.setFont(fieldFont);
        pwF.setFont(fieldFont);

        int y = 0;
        addRow(form, gbc, y++, "Name:", nameF);
        addRow(form, gbc, y++, "Land:", landF);
        addRow(form, gbc, y++, "Stadt:", stadtF);
        addRow(form, gbc, y++, "PLZ:", plzF);
        addRow(form, gbc, y++, "Straße:", strasseF);
        addRow(form, gbc, y++, "Benutzername:", userF);
        addRow(form, gbc, y++, "Passwort:", pwF);

        // Button-Bereich
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton registrieren = new JButton("Registrieren");
        JButton abbrechen = new JButton("Zurück");

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 16);
        registrieren.setFont(buttonFont);
        abbrechen.setFont(buttonFont);

        buttonPanel.add(registrieren);
        buttonPanel.add(abbrechen);
        add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener
        registrieren.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                String land = landF.getText().trim();
                String stadt = stadtF.getText().trim();
                String plz = plzF.getText().trim();
                String str = strasseF.getText().trim();
                String user = userF.getText().trim();
                String pw = new String(pwF.getPassword()).trim();

                if (name.isEmpty() || land.isEmpty() || stadt.isEmpty() || plz.isEmpty()
                        || str.isEmpty() || user.isEmpty() || pw.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen.", "Fehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!name.matches("[a-zA-ZäöüÄÖÜß\\s]+")) throw new LetterFormatException();
                if (!land.matches("[a-zA-ZäöüÄÖÜß\\s]+")) throw new LetterFormatException();
                if (!stadt.matches("[a-zA-ZäöüÄÖÜß\\s]+")) throw new LetterFormatException();
                if (!plz.matches("\\d+")) throw new NumberFormatException("PLZ hat nur Ziffern.");

                shop.registriereKunde(name, land, stadt, plz, str, user, pw);
                JOptionPane.showMessageDialog(this, "Registrierung erfolgreich.");
                gui.setzePanel(new LoginPanel(shop, gui));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        abbrechen.addActionListener(e -> gui.setzePanel(new LoginPanel(shop, gui)));
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
