package model;

import java.util.List;

/**
 * Represents a Pizza with various customizable components.
 */
public class Pizza {
    private String name;
    private String crust;
    private String sauce;
    private String cheese;
    private List<String> toppings; // Changed to List<String> for better flexibility
    private String size;  // Small, Medium, Large
    private double price; // Base price of the pizza

    // Private constructor for the Builder
    protected Pizza(PizzaBuilder builder) {
        this.name = builder.name;
        this.crust = builder.crust;
        this.sauce = builder.sauce;
        this.cheese = builder.cheese;
        this.toppings = builder.toppings;
        this.size = builder.size;
        this.price = builder.price;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCrust() {
        return crust;
    }

    public String getSauce() {
        return sauce;
    }

    public String getCheese() {
        return cheese;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String toppingsString = String.join(", ", toppings); // Convert List<String> to a comma-separated String
        return "Pizza: " + name +
                "\nSize: " + size +
                "\nCrust: " + crust +
                "\nSauce: " + sauce +
                "\nCheese: " + cheese +
                "\nToppings: " + toppingsString +
                "\nPrice: $" + String.format("%.2f", getPrice());
    }

    /**
     * Builder class for constructing a Pizza object.
     */
    public static class PizzaBuilder {
        private String name;
        private String crust;
        private String sauce;
        private String cheese;
        private List<String> toppings; // Updated to List<String>
        private String size;
        private double price;

        public PizzaBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PizzaBuilder setCrust(String crust) {
            this.crust = crust;
            return this;
        }

        public PizzaBuilder setSauce(String sauce) {
            this.sauce = sauce;
            return this;
        }

        public PizzaBuilder setCheese(String cheese) {
            this.cheese = cheese;
            return this;
        }

        public PizzaBuilder setToppings(List<String> toppings) {
            this.toppings = toppings;
            return this;
        }

        public PizzaBuilder setSize(String size) {
            this.size = size;
            return this;
        }

        public PizzaBuilder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}
