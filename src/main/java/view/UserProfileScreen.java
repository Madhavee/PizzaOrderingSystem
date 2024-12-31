package view;

import model.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Enhanced User Profile Screen with a modern design and back navigation.
 */
public class UserProfileScreen extends JFrame {

    private JTextField nameField, emailField, phoneField, addressField;
    private JLabel loyaltyPointsLabel;
    private JButton btnSave, btnBack;

    private final UserSession userSession;

    public UserProfileScreen() {
        userSession = UserSession.getInstance(); // Access user session

        setTitle("User Profile");
        setSize(500, 650); // Increased height to fit loyalty points section
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with Gradient Background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Header Section
        JLabel headerLabel = new JLabel("Your Profile", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Name:"), gbc);
        nameField = createRoundedTextField(userSession.getName());
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Email:"), gbc);
        emailField = createRoundedTextField(userSession.getEmail());
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Phone:"), gbc);
        phoneField = createRoundedTextField(userSession.getPhoneNumber());
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("Address:"), gbc);
        addressField = createRoundedTextField(userSession.getDeliveryAddress());
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        // Loyalty Points
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createLabel("Loyalty Points:"), gbc);
        loyaltyPointsLabel = new JLabel(String.valueOf(userSession.getLoyaltyPoints()), JLabel.CENTER);
        loyaltyPointsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loyaltyPointsLabel.setForeground(new Color(255, 215, 0)); // Gold color for points
        gbc.gridx = 1;
        formPanel.add(loyaltyPointsLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setOpaque(false);

        btnSave = createStyledButton("Save", "✔");
        btnBack = createStyledButton("Back", "⬅");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnBack);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button Listeners
        btnSave.addActionListener(e -> saveUserProfile());
        btnBack.addActionListener(e -> navigateBackToDashboard());

        setVisible(true);
    }

    /**
     * Creates a styled label for the form.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Creates a rounded text field with modern styling.
     */
    private JTextField createRoundedTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        textField.setBackground(new Color(240, 248, 255));
        textField.setForeground(new Color(33, 33, 33));
        textField.setOpaque(true);
        textField.setColumns(15);
        return textField;
    }

    /**
     * Creates a styled button with icons and rounded corners.
     */
    private JButton createStyledButton(String text, String iconText) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(76, 175, 80)); // Green
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    /**
     * Saves the user profile information to the UserSession.
     */
    private void saveUserProfile() {
        userSession.setName(nameField.getText().trim());
        userSession.setEmail(emailField.getText().trim());
        userSession.setPhoneNumber(phoneField.getText().trim());
        userSession.setDeliveryAddress(addressField.getText().trim());

        // Success message
        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh points dynamically
        refreshLoyaltyPoints();

        // Navigate back to the Dashboard
        navigateBackToDashboard();
    }

    /**
     * Refresh the loyalty points displayed on the screen.
     */
    private void refreshLoyaltyPoints() {
        loyaltyPointsLabel.setText(String.valueOf(userSession.getLoyaltyPoints()));
    }

    /**
     * Navigates back to the Dashboard screen.
     */
    private void navigateBackToDashboard() {
        new Dashboard();
        dispose();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserProfileScreen::new);
    }
}
