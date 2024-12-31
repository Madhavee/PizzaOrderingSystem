package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages available promotions and validates promo codes using Singleton Pattern.
 */
public class PromotionManager {
    private static PromotionManager instance; // Singleton instance
    private final List<Promotion> promotions; // Centralized promotion list
    private final List<PromotionManagerListener> listeners; // Listeners to notify UI components
    private static final String FILE_NAME = "promotions.dat"; // File for persistence

    /**
     * Private Constructor: Initializes the promotion list with default active promotions.
     */
    private PromotionManager() {
        listeners = new ArrayList<>();
        promotions = loadPromotions(); // Load promotions from file
        if (promotions.isEmpty()) {
            initializeDefaultPromotions(); // If no promotions found, initialize default ones
        }
    }

    /**
     * Provides the Singleton instance of PromotionManager.
     */
    public static synchronized PromotionManager getInstance() {
        if (instance == null) {
            instance = new PromotionManager();
        }
        return instance;
    }

    /**
     * Initializes a set of default promotions.
     */
    private void initializeDefaultPromotions() {
        promotions.add(new Promotion("HOLIDAY10", "Holiday Special - $10 Off", 10.0, true,
                LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 31), "ORDER"));
        promotions.add(new Promotion("SUMMER15", "Summer Deal - $15 Off", 15.0, true,
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), "TOPPING:Pineapple"));
        promotions.add(new Promotion("WELCOME5", "Welcome Offer - $5 Off", 5.0, true,
                null, null, "ORDER"));
        savePromotions(); // Save default promotions
    }

    /**
     * Fetches all active promotions based on their current state.
     *
     * @return List of currently active promotions.
     */
    public List<Promotion> getActivePromotions() {
        List<Promotion> activePromotions = new ArrayList<>();
        for (Promotion promo : promotions) {
            if (promo.isCurrentlyActive()) {
                activePromotions.add(promo);
            }
        }
        return activePromotions;
    }

    /**
     * Validates a promotion code.
     *
     * @param promoCode Promotion code.
     * @param condition Current order condition (e.g., "ORDER" or "TOPPING").
     * @return Promotion object if valid, otherwise null.
     */
    public Promotion validatePromoCode(String promoCode, String condition) {
        for (Promotion promo : promotions) {
            if (promo.isCurrentlyActive() && promo.getPromoCode().equalsIgnoreCase(promoCode)) {
                // Check the condition (if applicable)
                if (promo.getCondition().equalsIgnoreCase("ORDER") || promo.getCondition().equalsIgnoreCase(condition)) {
                    return promo; // Return the valid promotion
                }
            }
        }
        return null; // Return null if no valid promotion is found
    }

    /**
     * Adds a new promotion.
     *
     * @param promoCode      Unique promo code.
     * @param promoName      Description or name of the promotion.
     * @param discountAmount Discount amount.
     * @param isActive       Active status.
     * @param startDate      Start date of the promotion.
     * @param endDate        End date of the promotion.
     * @param condition      Promotion condition (e.g., "TOPPING:Pineapple").
     */
    public void addPromotion(String promoCode, String promoName, double discountAmount, boolean isActive,
                             LocalDate startDate, LocalDate endDate, String condition) {
        promotions.add(new Promotion(promoCode, promoName, discountAmount, isActive, startDate, endDate, condition));
        savePromotions(); // Save promotions after adding a new one
        notifyListeners(); // Notify all listeners about the update
    }

    /**
     * Deactivates a promotion by promo code.
     *
     * @param promoCode The promo code to deactivate.
     * @return True if successful, false otherwise.
     */
    public boolean deactivatePromotion(String promoCode) {
        for (Promotion promo : promotions) {
            if (promo.getPromoCode().equalsIgnoreCase(promoCode)) {
                promo.setActive(false); // Deactivate the promotion
                savePromotions(); // Save promotions after deactivating
                notifyListeners(); // Notify listeners about the update
                return true;
            }
        }
        return false; // Return false if no promotion with the specified code is found
    }

    /**
     * Removes a promotion by promo code.
     *
     * @param promoCode The promo code to remove.
     * @return True if the promotion was removed, false otherwise.
     */
    public boolean removePromotion(String promoCode) {
        for (int i = 0; i < promotions.size(); i++) {
            if (promotions.get(i).getPromoCode().equalsIgnoreCase(promoCode)) {
                promotions.remove(i); // Remove the promotion
                savePromotions(); // Save promotions after removal
                notifyListeners(); // Notify listeners about the update
                return true;
            }
        }
        return false; // Return false if no promotion with the specified code is found
    }

    /**
     * Fetches all promotions for administrative purposes.
     *
     * @return List of all promotions.
     */
    public List<Promotion> getAllPromotions() {
        return promotions;
    }

    /**
     * Saves the promotions list to a file.
     */
    private void savePromotions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(promotions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the promotions list from a file.
     *
     * @return List of promotions or an empty list if the file doesn't exist.
     */
    private List<Promotion> loadPromotions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Promotion>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }
    }

    /**
     * Adds a listener to notify when the promotion list is updated.
     *
     * @param listener Listener to add.
     */
    public void addListener(PromotionManagerListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener.
     *
     * @param listener Listener to remove.
     */
    public void removeListener(PromotionManagerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners about updates to the promotion list.
     */
    private void notifyListeners() {
        for (PromotionManagerListener listener : listeners) {
            listener.onPromotionsUpdated();
        }
    }

    /**
     * Listener interface to notify UI components about updates.
     */
    public interface PromotionManagerListener {
        void onPromotionsUpdated();
    }
}
