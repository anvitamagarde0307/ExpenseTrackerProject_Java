import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BarGraph extends JPanel {

    private Map<String, Integer> data;

    public BarGraph(Map<String, Integer> data) {
        this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int barWidth = width / data.size();

        int max = Collections.max(data.values());

        int x = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String date = entry.getKey();
            int amount = entry.getValue();
            int barHeight = (int) ((double) amount / max * (height - 20));
            int y = height - barHeight;

            g.setColor(Color.GRAY);
            g.fillRect(x, y, barWidth, barHeight);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, barHeight);

            // Display the date below the bar
            g.drawString(date, x + barWidth / 2 - 20, height - 5);

            // Display the amount on top of the bar
            g.drawString(String.valueOf(amount), x + barWidth / 2 - 10, y - 5);
            x += barWidth;
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> data = new LinkedHashMap<>();
        data.put("2024-05-01", 10);
        data.put("2024-05-02", 20);
        data.put("2024-05-03", 30);
        data.put("2024-05-04", 40);
        data.put("2024-05-05", 50);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bar Graph Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new BarGraph(data));
            frame.setSize(800, 400);
            frame.setVisible(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
            int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
            frame.setLocation(centerX, centerY);
        });
    }
}
