package model;

import model.Pizza;
import service.FavoriteManager;

/**
 * Command for saving a pizza to the favorite list.
 */
public class SavePizzaCommand implements command {

    private final Pizza pizza;

    public SavePizzaCommand(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public void execute() {
        FavoriteManager.addFavorite(pizza); // Adds the pizza to the favorite list
        System.out.println("Pizza saved to favorites: " + pizza.getName());
    }
}
