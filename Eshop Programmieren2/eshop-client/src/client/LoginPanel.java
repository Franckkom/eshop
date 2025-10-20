package client;

import common.model.Benutzer;
import common.model.Kunde;
import common.model.Mitarbeiter;

import javax.swing.*;
import java.awt.*;

/**
 * Login Panel zum Prüfen ob es ein Kunde oder Mitarbeiter ist
 */

public class LoginPanel extends JPanel {

    public LoginPanel(OnlineShopFassade shop, ShopClientGUI gui) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("eShop", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField benutzernameFeld = new JTextField(15);
        JPasswordField passwortFeld = new JPasswordField(15);

        // Benutzername
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Benutzername:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(benutzernameFeld, gbc);

        // Passwort
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Passwort:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passwortFeld, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginBtn = new JButton("Einloggen");
        JButton registrierenBtn = new JButton("Registrieren");
        buttonPanel.add(loginBtn);
        buttonPanel.add(registrierenBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Login
        loginBtn.addActionListener(e -> {
            String benutzername = benutzernameFeld.getText().trim();
            String passwort = new String(passwortFeld.getPassword()).trim();

            if (benutzername.isEmpty() || passwort.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte Benutzername und Passwort eingeben.");
                return;
            }

            try {

                Benutzer b = shop.login(benutzername, passwort);
                gui.setEingeloggterBenutzer(b);

                if (b instanceof Kunde) {
                    gui.setPanel("Artikel", new ArtikelPanel(gui));
                    gui.zeigePanel("Artikel");
                } else if (b instanceof Mitarbeiter m) {
                    gui.setPanel("MitarbeiterMenü", new MitarbeiterPanel(shop, m, gui));
                    gui.zeigePanel("MitarbeiterMenü");
                } else {
                    JOptionPane.showMessageDialog(this, "Unbekannter Benutzertyp.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login fehlgeschlagen: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        registrierenBtn.addActionListener(e -> gui.setzePanel(new RegistrierungPanel(shop, gui)));
    }
}

