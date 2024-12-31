package view;

import model.Order;
import model.Promotion;
import model.PromotionManager;
import model.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class PaymentScreen extends JFrame implements PromotionManager.PromotionManagerListener {

    private JComboBox<String> paymentMethodComboBox;
    private JComboBox<String> promoComboBox;
    private JTextField loyaltyPointsInputField;
    private JLabel orderTotalLabel;
    private JLabel loyaltyPointsLabel;
    private final Order order;
    private final PromotionManager promotionManager;
    private double orderTotal;
    private final DecimalFormat df = new DecimalFormat("#.##");

    public PaymentScreen(Order order, PromotionManager promotionManager) {
        this.order = order;
        this.promotionManager = promotionManager;
        this.orderTotal = order.getTotalPrice();

        // Register as a listener
        promotionManager.addListener(this);

        setTitle("Payment and Promotions");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        add(mainPanel);

        JLabel titleLabel = new JLabel("Payment and Promotions", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Order Total
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabel("Order Total:"), gbc);

        gbc.gridx = 1;
        orderTotalLabel = createLabel("$" + df.format(orderTotal));
        detailsPanel.add(orderTotalLabel, gbc);

        // Loyalty Points
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabel("Loyalty Points Available:"), gbc);

        gbc.gridx = 1;
        loyaltyPointsLabel = createLabel("" + UserSession.getInstance().getLoyaltyPoints());
        detailsPanel.add(loyaltyPointsLabel, gbc);

        // Input for Loyalty Points to Use
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabel("Enter Points to Use:"), gbc);

        gbc.gridx = 1;
        loyaltyPointsInputField = new JTextField();
        loyaltyPointsInputField.setColumns(10);
        detailsPanel.add(loyaltyPointsInputField, gbc);

        // Promotions
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabel("Select Promotion:"), gbc);

        gbc.gridx = 1;
        promoComboBox = new JComboBox<>();
        refreshPromotions();
        detailsPanel.add(promoComboBox, gbc);

        // Apply Promotion Button
        gbc.gridx = 0;
        gbc.gridy++;
        JButton btnApplyPromo = createStyledButton("Apply Promo", "https://img.icons8.com/color/48/checkmark.png");
        detailsPanel.add(btnApplyPromo, gbc);

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabel("Select Payment Method:"), gbc);

        gbc.gridx = 1;
        paymentMethodComboBox = new JComboBox<>(new String[]{"Credit Card", "Digital Wallet", "Cash on Delivery"});
        paymentMethodComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        detailsPanel.add(paymentMethodComboBox, gbc);

        // Buttons Panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setOpaque(false);

        JButton btnConfirmPayment = createStyledButton("Confirm Payment", "https://img.icons8.com/color/48/money.png");
        JButton btnBack = createStyledButton("Back", "https://img.icons8.com/color/48/undo.png");

        buttonsPanel.add(btnConfirmPayment);
        buttonsPanel.add(btnBack);
        detailsPanel.add(buttonsPanel, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Add action listeners
        btnApplyPromo.addActionListener(this::applyPromotion);
        btnConfirmPayment.addActionListener(this::confirmPayment);
        btnBack.addActionListener(e -> goBackToDashboard());
    }

    private void refreshPromotions() {
        promoComboBox.removeAllItems();
        List<Promotion> promotions = promotionManager.getActivePromotions();
        for (Promotion promo : promotions) {
            promoComboBox.addItem(promo.getPromoCode());
        }
    }

    private void applyPromotion(ActionEvent e) {
        String promoCode = (String) promoComboBox.getSelectedItem();

        if (promoCode == null) {
            JOptionPane.showMessageDialog(this, "Please select a promotion.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Promotion promo = promotionManager.validatePromoCode(promoCode, "ORDER");
        if (promo != null) {
            double discount = promo.getDiscountAmount();
            order.applyPromotion(discount);
            orderTotal = order.getTotalPrice();
            orderTotalLabel.setText("$" + df.format(orderTotal));
            loyaltyPointsLabel.setText("" + UserSession.getInstance().getLoyaltyPoints());
            JOptionPane.showMessageDialog(this, "Promotion Applied!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid promotion!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmPayment(ActionEvent e) {
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
        UserSession userSession = UserSession.getInstance();

        // Parse loyalty points input
        int pointsToUse = 0;
        try {
            pointsToUse = Integer.parseInt(loyaltyPointsInputField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid loyalty points input.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate and redeem loyalty points
        if (pointsToUse > userSession.getLoyaltyPoints()) {
            JOptionPane.showMessageDialog(this, "Insufficient loyalty points.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double discount = pointsToUse / 10.0; // Calculate discount from points
        orderTotal -= discount;
        userSession.redeemLoyaltyPoints(pointsToUse);

        // Add loyalty points earned for this purchase
        int newPointsEarned = calculateLoyaltyPoints(orderTotal);
        userSession.addLoyaltyPoints(newPointsEarned);

        // Update UI
        loyaltyPointsLabel.setText(String.valueOf(userSession.getLoyaltyPoints()));
        orderTotalLabel.setText("$" + df.format(orderTotal));

        // Display confirmation
        JOptionPane.showMessageDialog(this,
                "Payment Successful!\n" +
                        "Payment Method: " + paymentMethod + "\n" +
                        "Amount Paid: $" + df.format(orderTotal) + "\n" +
                        "Loyalty Points Used: " + pointsToUse + "\n" +
                        "Loyalty Points Earned: " + newPointsEarned,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        // Navigate to Dashboard
        new Dashboard();
        dispose(); // Close the payment screen
    }

    private void goBackToDashboard() {
        new Dashboard(); // Create and show the Dashboard screen
        dispose(); // Close the current Payment screen
    }

    private int calculateLoyaltyPoints(double totalAmount) {
        return (int) (totalAmount / 10); // Example: Earn 1 point per $10 spent
    }

    @Override
    public void onPromotionsUpdated() {
        refreshPromotions();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createStyledButton(String text, String iconUrl) {
        JButton button;
        try {
            ImageIcon icon = new ImageIcon(new URL(iconUrl));
            button = new JButton(text, new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            button = new JButton(text);
        }
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(76, 175, 80));
        button.setFocusPainted(false);
        return button;
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(13, 71, 161), getWidth(), getHeight(), new Color(21, 101, 192));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        // Example usage
        PromotionManager promotionManager = PromotionManager.getInstance();
        Order exampleOrder = new Order("ORDER123", "2024-12-31", 100.0, null, "Delivery", "123 Street");
        new PaymentScreen(exampleOrder, promotionManager);
    }
}
