package view;

import model.Order;
import model.PromotionManager;
import model.PaymentStrategy;
import model.CreditCardPayment;
import model.DigitalWalletPayment;
import model.CashPayment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Order Summary screen with enhanced layout and organized UI.
 */
public class OrderSummary extends JFrame {

    private JTextArea orderDetailsArea;
    private JLabel totalLabel;
    private JButton btnBack, btnConfirm;

    private Order order;
    private Map<String, PaymentStrategy> paymentOptions;
    private static PromotionManager promotionManager;

    public OrderSummary(Order order, PromotionManager promotionManager) {
        this.order = order;
        this.promotionManager = PromotionManager.getInstance();

        setTitle("Order Summary");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializePaymentOptions();
        buildUI();

        setVisible(true);
    }

    private void initializePaymentOptions() {
        paymentOptions = new HashMap<>();
        paymentOptions.put("Credit Card", new CreditCardPayment());
        paymentOptions.put("Digital Wallet", new DigitalWalletPayment());
        paymentOptions.put("Cash Payment", new CashPayment());
    }

    private void buildUI() {
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Header Section
        JLabel headerLabel = new JLabel("Order Summary", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIcon(createIcon("https://img.icons8.com/color/64/order-summary.png"));
        headerLabel.setVerticalTextPosition(JLabel.BOTTOM);
        headerLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Order Details Section
        orderDetailsArea = new JTextArea(10, 40);
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        orderDetailsArea.setText(getOrderDetails());
        orderDetailsArea.setBorder(BorderFactory.createLineBorder(new Color(21, 101, 192), 2));
        JScrollPane scrollPane = new JScrollPane(orderDetailsArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Section: Total Cost and Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        // Total Label
        totalLabel = new JLabel("Total Cost: $" + String.format("%.2f", order.getTotalPrice()), JLabel.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(Color.WHITE);
        bottomPanel.add(totalLabel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        btnBack = createStyledButton("Back", "https://img.icons8.com/color/48/go-back.png");
        btnConfirm = createStyledButton("Proceed to Payment", "https://img.icons8.com/color/48/credit-card.png");

        buttonPanel.add(btnBack);
        buttonPanel.add(btnConfirm);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        btnBack.addActionListener(e -> goBack());
        btnConfirm.addActionListener(e -> proceedToPayment());
    }

    private String getOrderDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getOrderId()).append("\n")
                .append("Date: ").append(order.getDate()).append("\n")
                .append("Delivery Option: ").append(order.getDeliveryOption()).append("\n")
                .append("Delivery Address: ").append(order.getDeliveryAddress()).append("\n\n");

        String pizzaDetails = (order.getPizza() != null) ? order.getPizza().toString() : "No Pizza Details Available";
        details.append("Pizza Details: \n").append(pizzaDetails);

        if (order.getAppliedPromotion() != null) {
            details.append("\n\nApplied Promotion: ").append(order.getAppliedPromotion().getPromoName())
                    .append("\nDiscount: $").append(order.getAppliedPromotion().getDiscountAmount());
        }

        return details.toString();
    }

    private void proceedToPayment() {
        new PaymentScreen(order, promotionManager);
        dispose();
    }

    private void goBack() {
        new PizzaCustomization(); // Assuming PizzaCustomization exists
        dispose();
    }

    private JButton createStyledButton(String text, String iconUrl) {
        JButton button;
        try {
            ImageIcon icon = new ImageIcon(new URL(iconUrl));
            button = new JButton(text, new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            button = new JButton(text);
        }
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(76, 175, 80));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(230, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private ImageIcon createIcon(String url) {
        try {
            return new ImageIcon(new URL(url));
        } catch (Exception e) {
            return null;
        }
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(21, 101, 192), getWidth(), getHeight(), new Color(13, 71, 161));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        PromotionManager promoManager = PromotionManager.getInstance();
        Order mockOrder = new Order("ORD12345", "2024-12-17", 30.0, null, "Delivery", "123 Test Street");
        SwingUtilities.invokeLater(() -> new OrderSummary(mockOrder, promoManager));
    }
}
