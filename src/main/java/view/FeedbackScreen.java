package view;

import model.Feedback;
import model.FeedbackManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * Allows customers to provide feedback and ratings with improved UI and alignment.
 */
public class FeedbackScreen extends JFrame {

    private final JTextField orderIdField, pizzaNameField;
    private final JComboBox<Integer> ratingComboBox;
    private final JTextArea commentsArea;
    private final JButton btnSubmit, btnBack, btnViewFeedbacks;
    private final FeedbackManager feedbackManager;

    public FeedbackScreen() {
        feedbackManager = FeedbackManager.getInstance(); // Use singleton instance
        setTitle("Feedback and Ratings");
        setSize(800, 800); // Adjusted size for spacing
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gradient Background Panel
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Header
        JLabel headerLabel = new JLabel("Feedback and Ratings", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIcon(createIcon("https://img.icons8.com/color/48/feedback.png"));
        headerLabel.setVerticalTextPosition(JLabel.BOTTOM);
        headerLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Order ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        orderIdField = createInputField();
        orderIdField.setPreferredSize(new Dimension(300, 40)); // Larger size for better visibility
        formPanel.add(orderIdField, gbc);

        // Pizza Name
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabel("Pizza Name:"), gbc);

        gbc.gridx = 1;
        pizzaNameField = createInputField();
        pizzaNameField.setPreferredSize(new Dimension(300, 40)); // Larger size for better visibility
        formPanel.add(pizzaNameField, gbc);

        // Rating
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabel("Rating (1-5):"), gbc);

        gbc.gridx = 1;
        ratingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ratingComboBox.setPreferredSize(new Dimension(300, 40)); // Larger combo box
        formPanel.add(ratingComboBox, gbc);

        // Comments
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(createLabel("Comments:"), gbc);

        gbc.gridx = 1;
        commentsArea = new JTextArea(5, 20);
        commentsArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        commentsArea.setBorder(BorderFactory.createLineBorder(new Color(21, 101, 192), 2));
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsArea.setPreferredSize(new Dimension(300, 100)); // Larger text area
        formPanel.add(new JScrollPane(commentsArea), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        btnSubmit = createStyledButton("Submit", "https://img.icons8.com/color/48/submit-progress.png");
        btnBack = createStyledButton("Back", "https://img.icons8.com/color/48/go-back.png");
        btnViewFeedbacks = createStyledButton("View Feedbacks", "https://img.icons8.com/color/48/view.png");

        Dimension buttonSize = new Dimension(180, 50);
        btnSubmit.setPreferredSize(buttonSize);
        btnBack.setPreferredSize(buttonSize);
        btnViewFeedbacks.setPreferredSize(buttonSize);

        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnViewFeedbacks);
        buttonPanel.add(btnBack);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        btnSubmit.addActionListener(e -> submitFeedback());
        btnBack.addActionListener(e -> goBackToDashboard());
        btnViewFeedbacks.addActionListener(e -> showFeedbacksDialog());

        setVisible(true);
    }

    private void submitFeedback() {
        String orderId = orderIdField.getText().trim();
        String pizzaName = pizzaNameField.getText().trim();
        Integer rating = (Integer) ratingComboBox.getSelectedItem();
        String comments = commentsArea.getText().trim();

        if (orderId.isEmpty() || pizzaName.isEmpty() || rating == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Feedback feedback = new Feedback(orderId, pizzaName, rating, comments);

        // Add feedback to FeedbackManager
        feedbackManager.addFeedback(feedback);

        JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void goBackToDashboard() {
        new Dashboard(); // Navigate to the Dashboard screen
        dispose();
    }

    private void showFeedbacksDialog() {
        JDialog feedbackDialog = new JDialog(this, "All Feedbacks", true);
        feedbackDialog.setSize(800, 600);
        feedbackDialog.setLocationRelativeTo(this);

        String[] columnNames = {"Order ID", "Pizza Name", "Rating", "Comments"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable feedbackTable = new JTable(model);

        feedbackTable.setRowHeight(30);
        feedbackTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        feedbackTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        feedbackTable.getTableHeader().setBackground(new Color(21, 101, 192));
        feedbackTable.getTableHeader().setForeground(Color.WHITE);

        // Fetch feedback list from FeedbackManager
        List<Feedback> feedbacks = feedbackManager.getAllFeedbacks();
        if (feedbacks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feedback available to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Feedback feedback : feedbacks) {
                model.addRow(new Object[]{
                        feedback.getOrderId(),
                        feedback.getPizzaName(),
                        feedback.getRating(),
                        feedback.getComments()
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        feedbackDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.addActionListener(e -> feedbackDialog.dispose());
        feedbackDialog.add(closeButton, BorderLayout.SOUTH);

        feedbackDialog.setVisible(true);
    }

    private void clearForm() {
        orderIdField.setText("");
        pizzaNameField.setText("");
        ratingComboBox.setSelectedIndex(0);
        commentsArea.setText("");
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
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
        SwingUtilities.invokeLater(FeedbackScreen::new);
    }
}
