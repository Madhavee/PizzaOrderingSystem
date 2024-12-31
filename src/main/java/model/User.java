package model;

/**
 * Represents a user in the pizza ordering system.
 */
public class User {
    private String name;
    private String email;
    private String phone;
    private int loyaltyPoints;

    public User(String name, String email, String phone, int loyaltyPoints) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.loyaltyPoints = loyaltyPoints;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
