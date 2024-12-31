package model;

public class OutForDeliveryState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new DeliveredState());
    }

    @Override
    public void prev(Order order) {
        order.setState(new InPreparationState());
    }

    @Override
    public String getStatus() {
        return "Out for Delivery";
    }
}
