package view;

import model.Order;
import model.Pizza;
import service.FavoriteManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavoritesWindow extends JFrame {
    private JTable table;
    private JButton btnReorder, btnClose, btnBack;
    private DefaultTableModel tableModel;

    public FavoritesWindow() {
        setTitle("Favorite Pizzas");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Panel with Gradient Background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Header Section with Online Icon
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setOpaque(false);

        try {
            // Load the pizza icon from an online source
            URL pizzaIconUrl = new URL("https://img.icons8.com/?size=100&id=23128&format=png&color=000000");
            ImageIcon pizzaIcon = new ImageIcon(new ImageIcon(pizzaIconUrl)
                    .getImage()
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(pizzaIcon);

            // Add the icon to the header
            headerPanel.add(iconLabel);
        } catch (Exception ex) {
            System.err.println("Failed to load pizza icon: " + ex.getMessage());
        }

        // Add the text to the header
        JLabel headerLabel = new JLabel("Your Favorite Pizzas", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Add header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Name", "Size", "Crust", "Sauce", "Cheese", "Toppings", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        styleTable();

        loadFavoritePizzas(); // Load favorite pizzas into the table

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setOpaque(false);

        btnReorder = createStyledButton("Reorder", "https://img.icons8.com/color/48/repeat.png");
        btnClose = createStyledButton("Close", "https://img.icons8.com/color/48/close-window.png");
        btnBack = createStyledButton("Back", "https://img.icons8.com/color/48/undo.png");

        buttonPanel.add(btnReorder);
        buttonPanel.add(btnClose);
        buttonPanel.add(btnBack);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        btnReorder.addActionListener(e -> reorderPizza());
        btnClose.addActionListener(e -> dispose());
        btnBack.addActionListener(e -> goBackToDashboard());

        setVisible(true);
    }

    /**
     * Loads favorite pizzas into the table.
     */
    private void loadFavoritePizzas() {
        List<Pizza> favorites = FavoriteManager.loadFavorites();
        tableModel.setRowCount(0); // Clear existing rows

        for (Pizza pizza : favorites) {
            String toppings = String.join(", ", pizza.getToppings()); // Convert List<String> to a comma-separated String
            tableModel.addRow(new Object[]{
                    pizza.getName(),
                    pizza.getSize(),
                    pizza.getCrust(),
                    pizza.getSauce(),
                    pizza.getCheese(),
                    toppings,
                    String.format("$%.2f", pizza.getPrice())
            });
        }
    }

    /**
     * Styles the table with modern design.
     */
    private void styleTable() {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.setRowHeight(35);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBackground(new Color(21, 101, 192));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Creates a styled button with an online icon.
     *
     * @param text    The button label.
     * @param iconUrl The URL of the icon.
     * @return A styled JButton.
     */
    private JButton createStyledButton(String text, String iconUrl) {
        JButton button;
        try {
            URL url = new URL(iconUrl);
            ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
            button = new JButton(text, icon);
        } catch (Exception e) {
            System.err.println("Icon not loaded for: " + text);
            button = new JButton(text);
        }

        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(76, 175, 80));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        JButton finalButton = button;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                finalButton.setBackground(new Color(67, 160, 71));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                finalButton.setBackground(new Color(76, 175, 80));
            }
        });
        return button;
    }

    /**
     * Handles the reorder pizza action.
     */
    private void reorderPizza() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String toppingsString = (String) tableModel.getValueAt(selectedRow, 5);
            List<String> toppings = List.of(toppingsString.split(", ")); // Convert String back to List<String>

            Pizza pizza = new Pizza.PizzaBuilder()
                    .setName((String) tableModel.getValueAt(selectedRow, 0))
                    .setSize((String) tableModel.getValueAt(selectedRow, 1))
                    .setCrust((String) tableModel.getValueAt(selectedRow, 2))
                    .setSauce((String) tableModel.getValueAt(selectedRow, 3))
                    .setCheese((String) tableModel.getValueAt(selectedRow, 4))
                    .setToppings(toppings)
                    .setPrice(Double.parseDouble(((String) tableModel.getValueAt(selectedRow, 6)).replace("$", "")))
                    .build();

            String orderId = "ORDER" + System.currentTimeMillis();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Order order = new Order(orderId, date, pizza.getPrice(), pizza, "Pickup", "N/A");

            new OrderSummary(order, null); // Redirect to Order Summary screen
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a pizza to reorder.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Navigates back to the dashboard screen.
     */
    private void goBackToDashboard() {
        new Dashboard(); // Open the dashboard screen
        dispose(); // Close the current window
    }

    /**
     * Gradient background panel for enhanced visual appeal.
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
        SwingUtilities.invokeLater(FavoritesWindow::new);
    }
}
