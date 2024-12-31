package model;

public class DeliveredState implements OrderState {

    @Override
    public void next(Order order) {
        // No next state from "Delivered"
        System.out.println("This is the final state. Can't go forward.");
    }

    @Override
    public void prev(Order order) {
        // Move back to the previous state: "Out for Delivery"
        order.setState(new OutForDeliveryState());
    }

    @Override
    public String getStatus() {
        // Return the name of the current state
        return "Delivered";
    }
}
