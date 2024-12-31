package model;

import model.Pizza;

import java.util.List;

public class ToppingsValidator implements ValidationHandler {
    private ValidationHandler nextHandler;

    @Override
    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean validate(Pizza pizza) {
        List<String> toppings = pizza.getToppings();
        if (toppings == null || toppings.isEmpty()) {
            System.out.println("Validation failed: At least one topping must be selected.");
            return false;
        }
        System.out.println("Toppings validation passed.");
        return nextHandler == null || nextHandler.validate(pizza);
    }
}
