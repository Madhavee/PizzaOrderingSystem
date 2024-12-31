package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

// Ensure these imports match the actual location of your classes
import model.UserSession;
import model.Order;
import model.PromotionManager;
import service.ScreenFactory;

/**
 * Dashboard class for the Pizza Ordering System.
 */
public class Dashboard extends JFrame {

    private JPanel mainPanel;

    public Dashboard() {
        this("Pizza Ordering System");
    }

    public Dashboard(String title) {
        // Check if the user is logged in
        if (!isUserLoggedIn()) {
            JOptionPane.showMessageDialog(this, "You must log in first!", "Access Denied", JOptionPane.WARNING_MESSAGE);
            new LoginScreen();
            dispose();
            return;
        }

        // Initialize JFrame
        setTitle(title);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Header
        JLabel titleLabel = new JLabel("Welcome to Your Pizza Hub!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(30, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Add styled buttons with icons
        buttonsPanel.add(createCardButton("Place Order", "https://img.icons8.com/color/96/pizza.png", "Pizza Customization"));
        buttonsPanel.add(createCardButton("Track Order", "https://img.icons8.com/color/96/delivery.png", "Order Tracking"));
        buttonsPanel.add(createCardButton("User Profile", "https://img.icons8.com/color/96/user-male.png", "User Profile"));
        buttonsPanel.add(createCardButton("Favorites", "https://img.icons8.com/color/96/like.png", "Favorites Management"));
        buttonsPanel.add(createCardButton("Payment", "https://img.icons8.com/color/96/money.png", "Payment Screen"));
        buttonsPanel.add(createCardButton("Feedback", "https://img.icons8.com/color/96/star.png", "Feedback Screen"));

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("<html><center>🍕 Thank you for choosing Pizza Hub! We hope you enjoy your experience. 🍕</center></html>", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Creates a card-like button with an icon, text, and hover effect.
     */
    private JButton createCardButton(String text, String iconUrl, String screenName) {
        JButton button = new JButton("<html><center>" + "<img src='" + iconUrl + "'><br><br>" + "<span style='font-size:18px;font-weight:bold;'>" + text + "</span></center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185)); // Soft blue
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Add shadow effect
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(31, 97, 141)); // Darker blue
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
        });

        // Button action
        button.addActionListener((ActionEvent e) -> navigateTo(screenName));
        return button;
    }

    /**
     * Checks if the user is logged in.
     */
    private boolean isUserLoggedIn() {
        try {
            return UserSession.isLoggedIn();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Handles navigation to different screens using ScreenFactory.
     */
    private void navigateTo(String screenName) {
        try {
            JFrame nextScreen;
            if (screenName.equals("Payment Screen")) {
                // Pass required objects for PaymentScreen
                Order currentOrder = new Order("ORD123", "2024-12-31", 50.0, null, "Pickup", "123 Street");
                nextScreen = new PaymentScreen(currentOrder, PromotionManager.getInstance());
            } else {
                // Use ScreenFactory for other screens
                nextScreen = ScreenFactory.createScreen(screenName);
            }

            if (nextScreen != null) {
                nextScreen.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: Could not navigate to " + screenName,
                        "Navigation Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred while navigating to " + screenName + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Gradient background panel.
     */
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(46, 204, 113), getWidth(), getHeight(), new Color(52, 152, 219));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Main method to run the dashboard.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
