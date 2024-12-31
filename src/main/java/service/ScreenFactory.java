package service;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ScreenFactory {

    private static final Map<String, Class<? extends JFrame>> screenMap = new HashMap<>();

    static {
        // Map screen names to their respective classes
        screenMap.put("Pizza Customization", view.PizzaCustomization.class);
        screenMap.put("Order Tracking", view.OrderTracking.class);
        screenMap.put("User Profile", view.UserProfileScreen.class);
        screenMap.put("Favorites Management", view.FavoritesWindow.class);
        screenMap.put("Payment Screen", view.PaymentScreen.class);
        screenMap.put("Feedback Screen", view.FeedbackScreen.class);
    }

    public static JFrame createScreen(String screenName) {
        try {
            Class<? extends JFrame> screenClass = screenMap.get(screenName);
            if (screenClass != null) {
                return screenClass.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if the screen is not found or there is an error
    }
}
