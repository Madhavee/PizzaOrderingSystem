package model;

public class OrderPlacedState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new InPreparationState());
    }

    @Override
    public void prev(Order order) {
        System.out.println("Order is already in the initial state.");
    }

    @Override
    public String getStatus() {
        return "Order Placed";
    }
}
