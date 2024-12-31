package view;

import model.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Login Screen for User Authentication with username, email, or phone.
 */
public class LoginScreen extends JFrame {

    private JTextField identifierField;
    private JPasswordField passwordField;
    private JButton btnLogin, btnExit;

    private static final Map<String, String> userDatabase = new HashMap<>();

    static {
        userDatabase.put("admin", "1234");               // Username
        userDatabase.put("admin@example.com", "1234");   // Email
        userDatabase.put("1234567890", "1234");          // Phone
    }

    public LoginScreen() {
        setTitle("Login - Pizza Shop");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Main Panel with Gradient
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Header Panel
        JLabel headerLabel = new JLabel("Welcome to Pizza Shop!", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIcon(createIcon("https://img.icons8.com/color/48/pizza.png"));
        headerLabel.setVerticalTextPosition(JLabel.BOTTOM);
        headerLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username/Email/Phone Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Username / Email / Phone:"), gbc);

        gbc.gridx = 1;
        identifierField = createRoundedInputField();
        identifierField.setPreferredSize(new Dimension(200, 40)); // Adjusted size
        formPanel.add(identifierField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = createRoundedPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 40)); // Adjusted size
        formPanel.add(passwordField, gbc);

        // Buttons Panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        btnLogin = createStyledButton("Login", "https://img.icons8.com/color/48/login-rounded-right.png");
        btnExit = createStyledButton("Exit", "https://img.icons8.com/color/48/cancel.png");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Footer Panel
        JLabel footerLabel = new JLabel("Enjoy customizing your favorite pizza!", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        footerLabel.setForeground(Color.WHITE);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        // Button Actions
        btnLogin.addActionListener(this::handleLogin);
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String identifier = identifierField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (validateUser(identifier, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            UserSession.logIn("John Doe", identifier);
            new Dashboard();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateUser(String identifier, String password) {
        return userDatabase.containsKey(identifier) && userDatabase.get(identifier).equals(password);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createRoundedInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(21, 101, 192), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createRoundedPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(21, 101, 192), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
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
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
