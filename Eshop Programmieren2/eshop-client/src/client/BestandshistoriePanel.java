package client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Klasse um Bestandshistorie der letzten 30 Tagen als Grafik zu zeigen
 * Rechts ist der aktuelle Tag
 */

public class BestandshistoriePanel extends JPanel {

    private final List<Integer> bestandswerte;  // 30 Werte: Tag 1 bis Tag 30

    public BestandshistoriePanel(List<Integer> bestandswerte) {
        this.bestandswerte = bestandswerte;
        setPreferredSize(new Dimension(900, 500));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bestandswerte == null || bestandswerte.size() < 2) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 60;
        int labelPadding = 30;

        int graphWidth = width - 2 * padding;
        int graphHeight = height - 2 * padding;


        int maxBestand = bestandswerte.stream().max(Integer::compareTo).orElse(10);
        maxBestand = ((maxBestand + 4) / 5) * 5;

        int pointCount = bestandswerte.size();
        int pointSpacing = graphWidth / (pointCount - 1);


        // Y-Achse
        g2.drawLine(padding, padding, padding, height - padding);
        g2.drawLine(padding, height - padding, width - padding, height - padding);

        // Y Beschriftung
        int ySteps = 5;
        for (int i = 0; i <= ySteps; i++) {
            int y = height - padding - i * graphHeight / ySteps;
            String label = String.valueOf(i * maxBestand / ySteps);
            g2.drawLine(padding - 5, y, padding + 5, y);
            g2.drawString(label, padding - labelPadding, y + 5);
        }
        g2.drawString("Bestand", padding - 40, padding - 20);

        // X Beschriftung
        for (int i = 0; i < pointCount; i++) {
            int x = padding + i * pointSpacing;
            g2.drawLine(x, height - padding - 5, x, height - padding + 5);
            if ((i + 1) % 5 == 0 || i == 0 || i == pointCount - 1) {
                g2.drawString(String.valueOf(i + 1), x - 5, height - padding + 20);
            }
        }
        g2.drawString("Tag", width - padding, height - padding + 40);

        // Linien zeichnen
        g2.setStroke(new BasicStroke(2));
        for (int i = 1; i < pointCount; i++) {
            int x1 = padding + (i - 1) * pointSpacing;
            int y1 = height - padding - (bestandswerte.get(i - 1) * graphHeight / maxBestand);
            int x2 = padding + i * pointSpacing;
            int y2 = height - padding - (bestandswerte.get(i) * graphHeight / maxBestand);
            g2.drawLine(x1, y1, x2, y2);
        }

        // Punkte zeichnen
        for (int i = 0; i < pointCount; i++) {
            int x = padding + i * pointSpacing;
            int y = height - padding - (bestandswerte.get(i) * graphHeight / maxBestand);
            g2.fillOval(x - 3, y - 3, 6, 6);
        }


    }
}
