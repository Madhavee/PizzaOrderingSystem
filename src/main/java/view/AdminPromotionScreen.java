package view;

import model.Promotion;
import model.PromotionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Admin screen to manage promotions (add, edit, view, and deactivate promotions).
 */
public class AdminPromotionScreen extends JFrame {

    private final PromotionManager promotionManager; // Singleton instance of PromotionManager
    private final JTable promoTable;
    private final DefaultTableModel tableModel;

    public AdminPromotionScreen() {
        this.promotionManager = PromotionManager.getInstance(); // Use Singleton instance

        // Frame setup
        setTitle("Admin - Promotion Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main Panel with Gradient Background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Header
        JLabel headerLabel = new JLabel("Promotion Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIcon(createIcon("https://img.icons8.com/color/64/discount.png"));
        headerLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        headerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Promo Code", "Description", "Discount ($)", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0);
        promoTable = new JTable(tableModel);
        promoTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        promoTable.setRowHeight(30);
        promoTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        promoTable.getTableHeader().setBackground(new Color(76, 175, 80));
        promoTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(promoTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(21, 101, 192), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Add Promotion", "https://img.icons8.com/color/48/add.png");
        JButton btnDeactivate = createStyledButton("Deactivate Promotion", "https://img.icons8.com/color/48/remove.png");
        JButton btnRefresh = createStyledButton("Refresh Table", "https://img.icons8.com/color/48/refresh.png");
        JButton btnRemove = createStyledButton("Remove Promotion", "https://img.icons8.com/color/48/delete-forever.png");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDeactivate);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnRemove);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        btnAdd.addActionListener(this::handleAddPromotion);
        btnDeactivate.addActionListener(this::handleDeactivatePromotion);
        btnRefresh.addActionListener(e -> refreshTable());
        btnRemove.addActionListener(this::handleRemovePromotion);

        // Register PromotionManager listener for dynamic updates
        promotionManager.addListener(this::refreshTable);

        // Load initial table data
        refreshTable();

        setVisible(true);
    }

    /**
     * Handles adding a new promotion.
     */
    private void handleAddPromotion(ActionEvent e) {
        try {
            String promoCode = JOptionPane.showInputDialog(this, "Enter Promo Code:");
            if (promoCode == null || promoCode.trim().isEmpty()) return;

            String description = JOptionPane.showInputDialog(this, "Enter Promo Description:");
            if (description == null || description.trim().isEmpty()) return;

            String discountStr = JOptionPane.showInputDialog(this, "Enter Discount Amount:");
            if (discountStr == null || discountStr.trim().isEmpty()) return;

            double discount = Double.parseDouble(discountStr);

            String startDateStr = JOptionPane.showInputDialog(this, "Enter Start Date (YYYY-MM-DD) (Optional):");
            LocalDate startDate = startDateStr == null || startDateStr.trim().isEmpty() ? null : LocalDate.parse(startDateStr);

            String endDateStr = JOptionPane.showInputDialog(this, "Enter End Date (YYYY-MM-DD) (Optional):");
            LocalDate endDate = endDateStr == null || endDateStr.trim().isEmpty() ? null : LocalDate.parse(endDateStr);

            String condition = JOptionPane.showInputDialog(this, "Enter Promotion Condition (e.g., TOPPING:Olives) (Optional):");
            if (condition == null || condition.trim().isEmpty()) condition = "ORDER";

            promotionManager.addPromotion(promoCode, description, discount, true, startDate, endDate, condition);
            JOptionPane.showMessageDialog(this, "Promotion added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid discount value. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter date as YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles deactivating a promotion.
     */
    private void handleDeactivatePromotion(ActionEvent e) {
        int selectedRow = promoTable.getSelectedRow();
        if (selectedRow != -1) {
            String promoCode = (String) tableModel.getValueAt(selectedRow, 0);
            if (promotionManager.deactivatePromotion(promoCode)) {
                JOptionPane.showMessageDialog(this, "Promotion deactivated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not deactivate promotion.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a promotion to deactivate.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Handles removing a promotion.
     */
    private void handleRemovePromotion(ActionEvent e) {
        int selectedRow = promoTable.getSelectedRow();
        if (selectedRow != -1) {
            String promoCode = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this promotion?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (promotionManager.removePromotion(promoCode)) {
                    tableModel.removeRow(selectedRow); // Remove from the table
                    JOptionPane.showMessageDialog(this, "Promotion removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not remove promotion.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a promotion to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Refreshes the table data with all promotions.
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing data
        for (Promotion promo : promotionManager.getAllPromotions()) {
            tableModel.addRow(new Object[]{
                    promo.getPromoCode(),
                    promo.getPromoName(),
                    promo.getDiscountAmount(),
                    promo.isCurrentlyActive() ? "Yes" : "No"
            });
        }
    }

    /**
     * Creates a styled button with an online icon.
     */
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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates an online icon.
     */
    private ImageIcon createIcon(String url) {
        try {
            return new ImageIcon(new URL(url));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gradient background panel.
     */
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
        SwingUtilities.invokeLater(AdminPromotionScreen::new);
    }
}
