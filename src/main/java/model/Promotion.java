package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a single promotion with conditions and time-based activation.
 */
public class Promotion implements Serializable { // Added Serializable interface
    private static final long serialVersionUID = 1L; // Added serialVersionUID for version control

    private String promoCode;      // Unique promo code
    private String promoName;      // Promotion name or description
    private double discountAmount; // Discount value
    private boolean isActive;      // Manual active status
    private LocalDate startDate;   // Start date of the promotion
    private LocalDate endDate;     // End date of the promotion
    private String condition;      // Condition (e.g., "TOPPING:Olives", "SIZE:Large")

    /**
     * Constructor to initialize a Promotion object.
     */
    public Promotion(String promoCode, String promoName, double discountAmount, boolean isActive,
                     LocalDate startDate, LocalDate endDate, String condition) {
        this.promoCode = promoCode;
        this.promoName = promoName;
        this.discountAmount = discountAmount;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.condition = condition;
    }

    // Getters and setters for all fields
    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Check if the promotion is currently active based on dates and manual status.
     */
    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return isActive && (startDate == null || !today.isBefore(startDate)) &&
                (endDate == null || !today.isAfter(endDate));
    }

    @Override
    public String toString() {
        return "Promotion Code: " + promoCode +
                "\nName: " + promoName +
                "\nDiscount: $" + discountAmount +
                "\nCondition: " + condition +
                "\nStart Date: " + (startDate != null ? startDate.toString() : "None") +
                "\nEnd Date: " + (endDate != null ? endDate.toString() : "None") +
                "\nActive: " + (isCurrentlyActive() ? "Yes" : "No");
    }
}
