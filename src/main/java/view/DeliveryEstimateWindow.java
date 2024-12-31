package view;

import service.DeliveryEstimateService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Window to display estimated delivery time and the static map.
 */
public class DeliveryEstimateWindow extends JFrame {

    private final JLabel deliveryTimeLabel;
    private final JLabel mapLabel;
    private final DeliveryEstimateService deliveryService;

    public DeliveryEstimateWindow(String deliveryAddress) {
        setTitle("Delivery Estimate");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        deliveryService = new DeliveryEstimateService();

        // Main Panel with Gradient Background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Delivery Estimate", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        try {
            // Load the header icon
            URL iconURL = new URL("https://img.icons8.com/color/96/truck.png");
            Image icon = ImageIO.read(iconURL).getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            titleLabel.setIcon(new ImageIcon(icon));
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL for icon: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }

        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        titleLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Delivery Time Panel
        JPanel deliveryTimePanel = new JPanel();
        deliveryTimePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        deliveryTimePanel.setOpaque(false);

        deliveryTimeLabel = new JLabel("Fetching delivery time...");
        deliveryTimeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        deliveryTimeLabel.setForeground(Color.WHITE);

        deliveryTimePanel.add(deliveryTimeLabel);
        mainPanel.add(deliveryTimePanel, BorderLayout.NORTH);

        // Map Panel
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setOpaque(false);

        mapLabel = new JLabel("Loading map...", JLabel.CENTER);
        mapLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        mapLabel.setForeground(Color.WHITE);

        // Load and display map
        loadMap(deliveryAddress);

        mapPanel.add(mapLabel, BorderLayout.CENTER);
        mainPanel.add(mapPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        JButton closeButton = createButtonWithIcon("Close", "https://img.icons8.com/fluency/48/cancel.png");
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Loads the static map and delivery time for the given address.
     */
    private void loadMap(String deliveryAddress) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Fetch delivery time
                String deliveryTime = deliveryService.getDeliveryTime(deliveryAddress);
                deliveryTimeLabel.setText("Estimated Delivery Time: " + deliveryTime);

                // Fetch map
                String mapURL = deliveryService.getStaticMapURL(deliveryAddress, 800, 500, 2);
                URL imageURL = new URL(mapURL);
                ImageIcon mapIcon = new ImageIcon(imageURL);
                Image scaledImage = mapIcon.getImage().getScaledInstance(850, 450, Image.SCALE_SMOOTH);
                mapLabel.setIcon(new ImageIcon(scaledImage));
                mapLabel.setText(""); // Remove loading text
            } catch (MalformedURLException e) {
                mapLabel.setText("Error: Invalid map URL.");
                System.err.println("Invalid map URL: " + e.getMessage());
            } catch (Exception e) {
                mapLabel.setText("Error loading map.");
                System.err.println("Error loading map: " + e.getMessage());
            }
        });
    }

    /**
     * Creates a styled button with an icon and consistent UI.
     */
    private JButton createButtonWithIcon(String text, String iconUrl) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            URL iconURL = new URL(iconUrl);
            Image icon = ImageIO.read(iconURL).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(icon));
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL for button icon: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to load button icon: " + e.getMessage());
        }

        return button;
    }

    /**
     * Gradient background panel for a modern look.
     */
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(33, 150, 243), getWidth(), getHeight(), new Color(13, 71, 161));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
