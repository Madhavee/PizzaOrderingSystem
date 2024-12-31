package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private OrderState state;                // State management
    private String orderId;
    private String date;
    private double totalPrice;
    private Pizza pizza;
    private String deliveryOption;
    private String deliveryAddress;
    private String feedback;                 // User feedback
    private int rating;                      // User rating (1 to 5)
    private List<OrderObserver> observers;   // Observer list for notifications
    private Promotion appliedPromotion;      // Applied promotion

    // Constructor
    public Order(String orderId, String date, double totalPrice, Pizza pizza, String deliveryOption, String deliveryAddress) {
        this.state = new OrderPlacedState(); // Default state when order is created
        this.orderId = orderId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.pizza = pizza;
        this.deliveryOption = deliveryOption;
        this.deliveryAddress = deliveryAddress;
        this.feedback = "";
        this.rating = 0;
        this.observers = new ArrayList<>();
        this.appliedPromotion = null;
    }

    // State management methods
    public void nextState() {
        state.next(this);
        notifyObservers();
    }

    public void prevState() {
        state.prev(this);
        notifyObservers();
    }

    public String getCurrentStatus() {
        return state.getStatus();
    }

    public void setState(OrderState state) {
        this.state = state;
        notifyObservers();
    }

    // Apply Promotion Method
    public boolean applyPromotion(String promoCode) {
        PromotionManager promotionManager = PromotionManager.getInstance();
        Promotion promotion = promotionManager.validatePromoCode(promoCode, getPromoCondition());
        if (promotion != null) {
            this.appliedPromotion = promotion;
            applyPromotion(promotion.getDiscountAmount());
            return true; // Promotion applied successfully
        }
        return false; // Promotion invalid
    }

    public void applyPromotion(double discount) {
        if (discount < 0) {
            throw new IllegalArgumentException("Discount cannot be negative.");
        }
        this.totalPrice = Math.max(0, this.totalPrice - discount); // Ensures price doesn't drop below 0
    }

    // Feedback and Rating Methods
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    // Observer Management
    public void addObserver(OrderObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.update(this);
        }
    }

    // Promotion Condition Helper
    private String getPromoCondition() {
        if (pizza != null && pizza.getToppings() != null && !pizza.getToppings().isEmpty()) {
            return "TOPPING:" + pizza.getToppings();
        }
        return "ORDER";
    }

    // Utility method to simulate time-based updates for tracking
    public void simulateOrderProgress() {
        nextState();
        notifyObservers();
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = Math.max(0, totalPrice);
    }

    public Pizza getPizza() {
        return pizza;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Promotion getAppliedPromotion() {
        return appliedPromotion;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                "\nDate: " + date +
                "\nPizza: " + (pizza != null ? pizza.toString() : "No Pizza Details") +
                "\nDelivery Option: " + deliveryOption +
                "\nDelivery Address: " + deliveryAddress +
                "\nTotal Price: $" + String.format("%.2f", totalPrice) +
                (appliedPromotion != null ? "\nApplied Promotion: " + appliedPromotion.getPromoName() : "") +
                "\nCurrent Status: " + getCurrentStatus() +
                "\nFeedback: " + (feedback.isEmpty() ? "No feedback provided" : feedback) +
                "\nRating: " + (rating > 0 ? rating + " stars" : "No rating given");
    }
}
