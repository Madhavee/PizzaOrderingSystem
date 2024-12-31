package model;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
        return true; // Mock success
    }

    @Override
    public String getPaymentType() {
        return "Credit Card";
    }
}
