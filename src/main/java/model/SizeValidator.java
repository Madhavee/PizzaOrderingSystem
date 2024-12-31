package model;

import model.Pizza;

public class SizeValidator implements ValidationHandler {
    private ValidationHandler nextHandler;

    @Override
    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(Pizza pizza) {
        if (pizza.getSize() == null || pizza.getSize().isEmpty()) {
            System.out.println("Validation failed: Size is not selected.");
            return false;
        }
        System.out.println("Size validation passed.");
        return nextHandler == null || nextHandler.validate(pizza);
    }
}
