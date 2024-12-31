package controller;

import model.User;

public class UserController {
    private User user;

    public UserController() {
        // Mock user for testing; replace with persistence logic later
        this.user = new User("John Doe", "john.doe@example.com", "123-456-7890", 150);
    }

    /**
     * Get the user profile.
     *
     * @return The current User object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Update user details with new information.
     *
     * @param name  The updated name.
     * @param email The updated email.
     * @param phone The updated phone number.
     */
    public void updateUserProfile(String name, String email, String phone) {
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
    }

    /**
     * Get loyalty points.
     *
     * @return The current loyalty points of the user.
     */
    public int getLoyaltyPoints() {
        return user.getLoyaltyPoints();
    }

    /**
     * Add loyalty points after a purchase.
     *
     * @param points Points to be added.
     */
    public void addLoyaltyPoints(int points) {
        user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
    }
}
