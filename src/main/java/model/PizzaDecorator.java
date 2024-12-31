package model;

import model.Pizza;

/**
 * Abstract decorator class for adding optional features to Pizza.
 */
public abstract class PizzaDecorator extends Pizza {
    protected Pizza pizza;

    public PizzaDecorator(Pizza pizza) {
        super(new Pizza.PizzaBuilder()
                .setName(pizza.getName())
                .setCrust(pizza.getCrust())
                .setSauce(pizza.getSauce())
                .setCheese(pizza.getCheese())
                .setToppings(pizza.getToppings())
                .setSize(pizza.getSize())
                .setPrice(pizza.getPrice()));
        this.pizza = pizza;
    }

    @Override
    public abstract String getName();

    @Override
    public abstract double getPrice();

    @Override
    public String toString() {
        return pizza.toString(); // Delegate to the wrapped pizza
    }
}
