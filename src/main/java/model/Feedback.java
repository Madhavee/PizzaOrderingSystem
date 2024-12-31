package model;

/**
 * Represents user feedback for an order.
 */
public class Feedback {
    private String orderId;
    private String pizzaName;
    private int rating; // 1 to 5 stars
    private String comments;

    public Feedback(String orderId, String pizzaName, int rating, String comments) {
        this.orderId = orderId;
        this.pizzaName = pizzaName;
        this.rating = rating;
        this.comments = comments;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }
}
