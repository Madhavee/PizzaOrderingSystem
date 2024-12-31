package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class StorageManager {

    private static final String ORDER_FILE = "orders.json";
    private static final String FAVORITES_FILE = "favorites.json";
    private static final Gson gson = new Gson();

    // Save orders to file
    public static <T> void saveDataToFile(List<T> data, String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load orders from file
    public static <T> List<T> loadDataFromFile(String filePath, Type type) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            return null; // Return null if the file doesn't exist or there's an error
        }
    }

    // Save orders
    public static void saveOrders(List<model.Order> orders) {
        saveDataToFile(orders, ORDER_FILE);
    }

    // Load orders
    public static List<model.Order> loadOrders() {
        Type listType = new TypeToken<List<model.Order>>() {}.getType();
        return loadDataFromFile(ORDER_FILE, listType);
    }

    // Save favorites
    public static void saveFavorites(List<model.Pizza> favorites) {
        saveDataToFile(favorites, FAVORITES_FILE);
    }

    // Load favorites
    public static List<model.Pizza> loadFavorites() {
        Type listType = new TypeToken<List<model.Pizza>>() {}.getType();
        return loadDataFromFile(FAVORITES_FILE, listType);
    }
}
