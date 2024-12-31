package model;

import model.Pizza;

/**
 * Adds extra cheese to the pizza.
 */
public class ExtraCheeseDecorator extends PizzaDecorator {

    public ExtraCheeseDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getName() {
        return pizza.getName() + " with Extra Cheese";
    }

    @Override
    public double getPrice() {
        return pizza.getPrice() + 1.5; // Add extra cheese cost
    }

    @Override
    public String toString() {
        return super.toString() + "\nFeature: Extra Cheese";
    }
}
