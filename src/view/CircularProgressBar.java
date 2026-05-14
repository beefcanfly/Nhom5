package view;

import javax.swing.*;
import java.awt.*;

public class CircularProgressBar extends JPanel {
    private double percent = 0;
    private String title = "";
    private Color color;

    public CircularProgressBar(String title, Color color) {
        this.title = title;
        this.color = color;
        setPreferredSize(new Dimension(280, 280));
        setBackground(Color.WHITE);
    }

    public void updateValue(int part, int total) {
        this.percent = (total > 0) ? ((double) part / total) * 100 : 0;
        repaint(); // Vẽ lại khi có dữ liệu mới
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = 160;
        int x = (getWidth() - size) / 2;
        int y = 40;

        // 1. Vẽ vòng nền xám
        g2.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(240, 240, 240));
        g2.drawOval(x, y, size, size);

        // 2. Vẽ vòng tiến độ
        g2.setColor(color);
        int angle = (int) (percent * 3.6);
        g2.drawArc(x, y, size, size, 90, -angle);

        // 3. Vẽ chữ % ở giữa
        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        String txt = String.format("%.1f%%", percent);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, y + (size / 2) + 10);

        // 4. Vẽ tiêu đề dưới vòng tròn
        g2.setColor(new Color(100, 100, 100));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2.drawString(title, (getWidth() - g2.getFontMetrics().stringWidth(title)) / 2, y + size + 50);
    }
}