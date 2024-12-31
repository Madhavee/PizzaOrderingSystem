package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import model.Pizza;

/**
 * Manages saving and retrieving favorite pizza combinations.
 */
public class FavoriteManager {
    private static final String FILE_PATH = "favorites.json";
    private static List<Pizza> favorites = new ArrayList<>();
    private static final Gson gson = new Gson();

    // Load favorites from the JSON file
    public static List<Pizza> loadFavorites() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            favorites = gson.fromJson(reader, new TypeToken<List<Pizza>>(){}.getType());
            if (favorites == null) favorites = new ArrayList<>();
        } catch (Exception e) {
            favorites = new ArrayList<>();
        }
        return favorites;
    }

    // Save favorites to the JSON file
    public static void saveFavorites() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(favorites, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add a pizza to favorites
    public static void addFavorite(Pizza pizza) {
        favorites.add(pizza);
        saveFavorites();
    }

    // Retrieve the list of favorite pizzas
    public static List<Pizza> getFavorites() {
        return favorites;
    }
}
