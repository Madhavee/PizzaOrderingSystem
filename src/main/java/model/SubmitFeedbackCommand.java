package model;

public class SubmitFeedbackCommand implements command {
    private Feedback feedback;

    public SubmitFeedbackCommand(FeedbackManager feedbackManager, Feedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public void execute() {
        System.out.println("Feedback submitted: " + feedback.toString());
        // Logic to save the feedback in a database or system (mocked here)
    }
}
