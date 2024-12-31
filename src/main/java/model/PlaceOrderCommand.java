package model;

import model.Order;

public class PlaceOrderCommand implements command {
    private Order order;

    public PlaceOrderCommand(Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Order placed: " + order.toString());
        // Logic to save the order in a database or system (mocked here)
    }
}
