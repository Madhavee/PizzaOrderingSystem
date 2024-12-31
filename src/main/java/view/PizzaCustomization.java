package view;

import model.ExtraCheeseDecorator;
import model.SpecialPackagingDecorator;
import model.Pizza;
import model.Order;
import service.FavoriteManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PizzaCustomization extends JFrame {

    private JComboBox<String> sizeComboBox, crustComboBox, sauceComboBox, cheeseComboBox;
    private JCheckBox[] toppingCheckBoxes;
    private JCheckBox extraCheeseCheckBox, specialPackagingCheckBox;
    private JTextField deliveryAddressField, pizzaNameField;
    private JRadioButton pickupOption, deliveryOption;
    private JButton btnEstimateDelivery, btnSaveFavorite, btnSave, btnCancel;
    private JLabel totalPriceLabel;

    private double basePrice;
    private double toppingPrice = 1.5; // Cost per topping
    private double currentPrice;

    public PizzaCustomization() {
        setTitle("Pizza Customization");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        setupHeader(mainPanel);
        setupForm(mainPanel);
        setupButtons(mainPanel);

        setVisible(true);
    }

    private void setupHeader(JPanel mainPanel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("Customize Your Pizza", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void setupForm(JPanel mainPanel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pizza Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Pizza Name:"), gbc);
        pizzaNameField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(pizzaNameField, gbc);

        // Size Selection
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Select Size:"), gbc);
        sizeComboBox = new JComboBox<>(new String[]{"Small - $10", "Medium - $15", "Large - $20"});
        sizeComboBox.addActionListener(e -> updatePrice());
        gbc.gridx = 1;
        formPanel.add(sizeComboBox, gbc);

        // Crust Selection
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Select Crust:"), gbc);
        crustComboBox = new JComboBox<>(new String[]{"Thin", "Thick", "Stuffed"});
        gbc.gridx = 1;
        formPanel.add(crustComboBox, gbc);

        // Sauce Selection
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Select Sauce:"), gbc);
        sauceComboBox = new JComboBox<>(new String[]{"Tomato", "Barbecue", "Garlic"});
        gbc.gridx = 1;
        formPanel.add(sauceComboBox, gbc);

        // Cheese Selection
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Select Cheese:"), gbc);
        cheeseComboBox = new JComboBox<>(new String[]{"Mozzarella", "Cheddar", "Vegan"});
        gbc.gridx = 1;
        formPanel.add(cheeseComboBox, gbc);

        // Toppings Selection
        setupToppings(formPanel, gbc);

        // Optional Features
        setupOptionalFeatures(formPanel, gbc);

        // Delivery Options
        setupDeliveryOptions(formPanel, gbc);

        // Total Price Display
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Total Price:"), gbc);
        totalPriceLabel = new JLabel("$" + String.format("%.2f", basePrice));
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalPriceLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        formPanel.add(totalPriceLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Initialize the price
        updatePrice();
    }

    private void setupToppings(JPanel formPanel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Select Toppings:"), gbc);

        JPanel toppingPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        toppingPanel.setOpaque(false);
        toppingCheckBoxes = new JCheckBox[]{
                new JCheckBox("Pepperoni"), new JCheckBox("Mushrooms"),
                new JCheckBox("Olives"), new JCheckBox("Onions"),
                new JCheckBox("Bacon"), new JCheckBox("Pineapple")
        };
        for (JCheckBox checkBox : toppingCheckBoxes) {
            checkBox.setOpaque(false);
            checkBox.setForeground(Color.WHITE);
            checkBox.addActionListener(e -> updatePrice());
            toppingPanel.add(checkBox);
        }
        gbc.gridx = 1;
        formPanel.add(toppingPanel, gbc);
    }

    private void setupOptionalFeatures(JPanel formPanel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Optional Features:"), gbc);

        JPanel optionalFeaturesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        optionalFeaturesPanel.setOpaque(false);

        extraCheeseCheckBox = new JCheckBox("Extra Cheese (+$1.50)");
        specialPackagingCheckBox = new JCheckBox("Special Packaging (+$2.00)");

        extraCheeseCheckBox.setOpaque(false);
        specialPackagingCheckBox.setOpaque(false);
        extraCheeseCheckBox.setForeground(Color.WHITE);
        specialPackagingCheckBox.setForeground(Color.WHITE);

        // Add Action Listeners to update price when toggled
        extraCheeseCheckBox.addActionListener(e -> updatePrice());
        specialPackagingCheckBox.addActionListener(e -> updatePrice());

        optionalFeaturesPanel.add(extraCheeseCheckBox);
        optionalFeaturesPanel.add(specialPackagingCheckBox);

        gbc.gridx = 1;
        formPanel.add(optionalFeaturesPanel, gbc);
    }

    private void setupDeliveryOptions(JPanel formPanel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Pickup or Delivery:"), gbc);

        pickupOption = new JRadioButton("Pickup", true);
        deliveryOption = new JRadioButton("Delivery");
        pickupOption.setOpaque(false);
        deliveryOption.setOpaque(false);
        pickupOption.setForeground(Color.WHITE);
        deliveryOption.setForeground(Color.WHITE);

        ButtonGroup deliveryGroup = new ButtonGroup();
        deliveryGroup.add(pickupOption);
        deliveryGroup.add(deliveryOption);

        JPanel deliveryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        deliveryPanel.setOpaque(false);
        deliveryPanel.add(pickupOption);
        deliveryPanel.add(deliveryOption);

        gbc.gridx = 1;
        formPanel.add(deliveryPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Delivery Address:"), gbc);
        deliveryAddressField = new JTextField();
        deliveryAddressField.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(deliveryAddressField, gbc);

        deliveryOption.addActionListener(e -> deliveryAddressField.setEnabled(true));
        pickupOption.addActionListener(e -> deliveryAddressField.setEnabled(false));
    }

    private void setupButtons(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        btnEstimateDelivery = createButtonWithIcon("Estimate Delivery", "https://img.icons8.com/fluency/48/clock.png");
        btnSaveFavorite = createButtonWithIcon("Add to Favorites", "https://img.icons8.com/fluency/48/like.png");
        btnSave = createButtonWithIcon("Save", "https://img.icons8.com/fluency/48/save.png");
        btnCancel = createButtonWithIcon("Cancel", "https://img.icons8.com/fluency/48/cancel.png");

        buttonPanel.add(btnEstimateDelivery);
        buttonPanel.add(btnSaveFavorite);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        btnEstimateDelivery.addActionListener(this::handleEstimateDelivery);
        btnSaveFavorite.addActionListener(this::saveAsFavorite);
        btnSave.addActionListener(this::savePizzaAndOpenOrderSummary);
        btnCancel.addActionListener(e -> dispose());

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updatePrice() {
        switch (sizeComboBox.getSelectedIndex()) {
            case 0 -> basePrice = 10.0;
            case 1 -> basePrice = 15.0;
            case 2 -> basePrice = 20.0;
        }
        double toppingCost = 0;
        for (JCheckBox checkBox : toppingCheckBoxes) {
            if (checkBox.isSelected()) toppingCost += toppingPrice;
        }
        currentPrice = basePrice + toppingCost;

        if (extraCheeseCheckBox.isSelected()) currentPrice += 1.50;
        if (specialPackagingCheckBox.isSelected()) currentPrice += 2.00;

        totalPriceLabel.setText("$" + String.format("%.2f", currentPrice));
    }

    private void handleEstimateDelivery(ActionEvent e) {
        if (pickupOption.isSelected()) {
            JOptionPane.showMessageDialog(this, "Estimate Delivery is only available for delivery option.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String address = deliveryAddressField.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the delivery address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Navigate to DeliveryEstimateWindow
            new DeliveryEstimateWindow(address);
            dispose(); // Close the PizzaCustomization window
        }
    }

    private void saveAsFavorite(ActionEvent e) {
        List<String> selectedToppings = new ArrayList<>();
        for (JCheckBox checkBox : toppingCheckBoxes) {
            if (checkBox.isSelected()) selectedToppings.add(checkBox.getText());
        }

        Pizza pizza = new Pizza.PizzaBuilder()
                .setName(pizzaNameField.getText().trim().isEmpty() ? "Custom Pizza" : pizzaNameField.getText().trim())
                .setCrust((String) crustComboBox.getSelectedItem())
                .setSauce((String) sauceComboBox.getSelectedItem())
                .setCheese((String) cheeseComboBox.getSelectedItem())
                .setSize((String) sizeComboBox.getSelectedItem())
                .setToppings(selectedToppings)
                .setPrice(currentPrice)
                .build();

        FavoriteManager.addFavorite(pizza);
        JOptionPane.showMessageDialog(this, "Pizza saved as favorite!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void savePizzaAndOpenOrderSummary(ActionEvent e) {
        List<String> selectedToppings = new ArrayList<>();
        for (JCheckBox checkBox : toppingCheckBoxes) {
            if (checkBox.isSelected()) selectedToppings.add(checkBox.getText());
        }

        Pizza pizza = new Pizza.PizzaBuilder()
                .setName(pizzaNameField.getText().trim().isEmpty() ? "Custom Pizza" : pizzaNameField.getText().trim())
                .setCrust((String) crustComboBox.getSelectedItem())
                .setSauce((String) sauceComboBox.getSelectedItem())
                .setCheese((String) cheeseComboBox.getSelectedItem())
                .setSize((String) sizeComboBox.getSelectedItem())
                .setToppings(selectedToppings)
                .setPrice(currentPrice)
                .build();

        if (extraCheeseCheckBox.isSelected()) pizza = new ExtraCheeseDecorator(pizza);
        if (specialPackagingCheckBox.isSelected()) pizza = new SpecialPackagingDecorator(pizza);

        String deliveryType = pickupOption.isSelected() ? "Pickup" : "Delivery";
        String deliveryAddress = deliveryOption.isSelected() ? deliveryAddressField.getText().trim() : "N/A";

        Order order = new Order(
                "ORDER" + System.currentTimeMillis(),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                pizza.getPrice(), pizza, deliveryType, deliveryAddress
        );

        new OrderSummary(order, null);
        dispose();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createButtonWithIcon(String text, String iconUrl) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);

        try {
            Image icon = Toolkit.getDefaultToolkit().createImage(new URL(iconUrl));
            button.setIcon(new ImageIcon(icon.getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            System.err.println("Icon not found or failed to load: " + ex.getMessage());
        }

        return button;
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
        SwingUtilities.invokeLater(PizzaCustomization::new);
    }
}
