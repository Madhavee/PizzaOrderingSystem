package model;

public class DigitalWalletPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing digital wallet payment: $" + amount);
        return true; // Mock success
    }

    @Override
    public String getPaymentType() {
        return "Digital Wallet";
    }
}
