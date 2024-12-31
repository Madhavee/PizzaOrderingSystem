package model;

public interface PaymentStrategy {
    boolean processPayment(double amount);
    String getPaymentType();
}
