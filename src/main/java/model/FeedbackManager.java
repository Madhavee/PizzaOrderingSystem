package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage feedback.
 */
public class FeedbackManager {

    private static FeedbackManager instance;
    private final List<Feedback> feedbackList;

    private FeedbackManager() {
        feedbackList = new ArrayList<>();
    }

    public static synchronized FeedbackManager getInstance() {
        if (instance == null) {
            instance = new FeedbackManager();
        }
        return instance;
    }

    public void addFeedback(Feedback feedback) {
        feedbackList.add(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return new ArrayList<>(feedbackList);
    }

    // Dummy data for testing
    public void populateDummyData() {
        feedbackList.add(new Feedback("ORDER001", "Pepperoni Pizza", 5, "Delicious!"));
        feedbackList.add(new Feedback("ORDER002", "Veggie Pizza", 4, "Could use less cheese."));
    }
}
