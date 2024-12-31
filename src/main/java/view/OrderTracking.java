package view;

import model.Order;
import model.Pizza;
import model.OrderObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Real-Time Order Tracking: Updates the order state automatically in real-time
 * and notifies the user at significant milestones.
 */
public class OrderTracking extends JFrame implements OrderObserver {

    private Order order;               // The order being tracked
    private JLabel statusLabel;        // Displays the current status
    private JProgressBar progressBar;  // Visual representation of the order state
    private JButton btnCancel;         // Allows users to cancel tracking
    private Timer timer;               // Timer to simulate real-time updates
    private int progressValue = 0;     // Tracks progress bar state

    /**
     * Default constructor for compatibility (e.g., ScreenFactory).
     * Initializes with a mock order for testing purposes.
     */
    public OrderTracking() {
        this(createMockOrder());
    }

    /**
     * Main constructor with an Order object.
     *
     * @param order The order to track in real-time.
     */
    public OrderTracking(Order order) {
        this.order = order;
        this.order.addObserver(this); // Add observer for state changes

        // Set up the JFrame
        setTitle("Real-Time Order Tracking");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with Gradient Background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Title Section
        JLabel titleLabel = new JLabel(" Tracking Your Order", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);

        // Status Label
        statusLabel = new JLabel("Current Status: " + order.getCurrentStatus(), JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        statusLabel.setForeground(Color.WHITE);
        contentPanel.add(statusLabel, BorderLayout.CENTER);

        // Progress Bar
        progressBar = new JProgressBar(0, 3);
        progressBar.setValue(progressValue);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        progressBar.setForeground(new Color(76, 175, 80)); // Green color
        progressBar.setBackground(Color.WHITE);
        contentPanel.add(progressBar, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Button Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        btnCancel = createStyledButton("Stop Tracking");
        btnCancel.addActionListener(e -> stopTracking());
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Start Real-Time Updates
        startRealTimeUpdates();

        setVisible(true);
    }

    /**
     * Simulates real-time updates using a Timer.
     */
    private void startRealTimeUpdates() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (progressValue < 3) {
                    progressValue++;
                    order.nextState(); // State progression
                    notifyIntermediateStates(progressValue);
                } else {
                    timer.cancel(); // Stop updates once "Delivered"
                    SwingUtilities.invokeLater(() -> {
                        showNotification("🎉 Your order has been delivered!");
                        navigateToFeedbackScreen();
                    });
                }
            }
        }, 0, 5000); // Updates every 5 seconds
    }

    /**
     * Displays notifications for intermediate states.
     */
    private void notifyIntermediateStates(int progress) {
        switch (progress) {
            case 1:
                showNotification("👨‍🍳 Your pizza is being prepared!");
                break;
            case 2:
                showNotification("🚚 Your pizza is out for delivery!");
                break;
        }
    }

    /**
     * Updates the UI components when the order state changes.
     */
    @Override
    public void update(Order updatedOrder) {
        statusLabel.setText("Current Status: " + updatedOrder.getCurrentStatus());
        progressBar.setValue(progressValue);
    }

    /**
     * Displays a notification for significant updates.
     */
    private void showNotification(String message) {
        JOptionPane.showMessageDialog(this, message, "Order Update", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Navigates to the FeedbackScreen after order delivery.
     */
    private void navigateToFeedbackScreen() {
        new FeedbackScreen();
        dispose();
    }

    /**
     * Stops tracking the order.
     */
    private void stopTracking() {
        if (timer != null) {
            timer.cancel();
            JOptionPane.showMessageDialog(this, "Order tracking stopped.", "Tracking Stopped", JOptionPane.WARNING_MESSAGE);
        }
        dispose();
    }

    /**
     * Creates a styled button.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(new Color(244, 67, 54)); // Red color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a mock Order for fallback or testing purposes.
     */
    private static Order createMockOrder() {
        Pizza pizza = new Pizza.PizzaBuilder()
                .setName("Mock Margherita")
                .setCrust("Thin")
                .setSauce("Tomato")
                .setCheese("Mozzarella")
                .build();

        return new Order("ORD12345", "2024-12-17", 25.0, pizza, "Delivery", "123 Pizza Street");
    }

    /**
     * Gradient background panel for enhanced visual appeal.
     */
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 150, 136), getWidth(), getHeight(), new Color(38, 166, 154));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
