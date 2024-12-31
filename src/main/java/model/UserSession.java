package model;

/**
 * Singleton class to store user session details and manage loyalty points.
 */
public class UserSession {
    private static UserSession instance;
    private String name;
    private String email;
    private String phoneNumber;
    private String deliveryAddress;
    private boolean loggedIn; // Tracks if the user is logged in
    private int loyaltyPoints; // Tracks the user's loyalty points

    private UserSession() {
        // Default user details (mocked for testing)
        this.name = "John Doe";
        this.email = "johndoe@example.com";
        this.phoneNumber = "123-456-7890";
        this.deliveryAddress = "123 Main Street, Kalutara, Sri Lanka";
        this.loggedIn = false; // Default to not logged in
        this.loyaltyPoints = 200; // Default loyalty points for demonstration
    }

    // Singleton instance getter
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Login and Logout methods
    public static boolean isLoggedIn() {
        return getInstance().loggedIn;
    }

    public static void logIn(String name, String email) {
        UserSession session = getInstance();
        session.loggedIn = true;
        session.name = name;
        session.email = email;
    }

    public static void logOut() {
        UserSession session = getInstance();
        session.loggedIn = false;
        session.name = null;
        session.email = null;
        session.phoneNumber = null;
        session.deliveryAddress = null;
        session.loyaltyPoints = 0; // Reset loyalty points on logout
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    /**
     * Adds loyalty points based on the order total.
     *
     * @param pointsEarned Points to add.
     */
    public void addLoyaltyPoints(int pointsEarned) {
        this.loyaltyPoints += pointsEarned;
    }

    /**
     * Redeems loyalty points. Reduces the user's total points if they have enough.
     *
     * @param pointsToRedeem Points to redeem.
     * @return True if points were successfully redeemed, false if insufficient points.
     */
    public boolean redeemLoyaltyPoints(int pointsToRedeem) {
        if (this.loyaltyPoints >= pointsToRedeem) {
            this.loyaltyPoints -= pointsToRedeem;
            return true;
        }
        return false; // Insufficient points
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", loggedIn=" + loggedIn +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }
}
