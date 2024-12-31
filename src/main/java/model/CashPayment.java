package model;

public class CashPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing cash payment at delivery/pickup: $" + amount);
        return true; // Mock success
    }

    @Override
    public String getPaymentType() {
        return "Cash Payment";
    }
}
