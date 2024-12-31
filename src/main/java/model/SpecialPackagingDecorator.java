package model;

import model.Pizza;

/**
 * Adds special packaging to the pizza.
 */
public class SpecialPackagingDecorator extends PizzaDecorator {

    public SpecialPackagingDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getName() {
        return pizza.getName() + " with Special Packaging";
    }

    @Override
    public double getPrice() {
        return pizza.getPrice() + 2.0; // Add special packaging cost
    }

    @Override
    public String toString() {
        return super.toString() + "\nFeature: Special Packaging";
    }
}
