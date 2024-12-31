package model;

public class InPreparationState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new OutForDeliveryState());
    }

    @Override
    public void prev(Order order) {
        order.setState(new OrderPlacedState());
    }

    @Override
    public String getStatus() {
        return "In Preparation";
    }
}
