package model;

import model.Pizza;

public class CrustValidator implements ValidationHandler {
    private ValidationHandler nextHandler;

    @Override
    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(Pizza pizza) {
        if (pizza.getCrust() == null || pizza.getCrust().isEmpty()) {
            System.out.println("Validation failed: Crust is not selected.");
            return false;
        }
        System.out.println("Crust validation passed.");
        return nextHandler == null || nextHandler.validate(pizza);
    }
}
