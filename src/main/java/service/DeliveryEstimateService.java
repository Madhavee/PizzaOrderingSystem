package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStreamReader;

/**
 * Service for fetching delivery time and static map URLs using Google Maps APIs.
 */
public class DeliveryEstimateService {

    private static final String API_KEY = "AIzaSyBkLQRw8F8r34ww-xP47rZw0QC-enA7Ntc";
    private static final String SHOP_LOCATION = "Kalutara+Town,+Sri+Lanka";
    private static final String STATIC_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";
    private static final String DIRECTIONS_API_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";

    /**
     * Fetches estimated delivery time using Google Directions API.
     *
     * @param destination The delivery address.
     * @return Delivery time string, or an error message.
     */
    public String getDeliveryTime(String destination) {
        String formattedDestination = formatAddress(destination);
        String url = buildDirectionsURL(formattedDestination);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(url))) {

            JsonObject json = parseResponse(response);

            // Extract the duration field
            JsonObject legs = json.getAsJsonArray("routes").get(0)
                    .getAsJsonObject().getAsJsonArray("legs").get(0).getAsJsonObject();

            return legs.getAsJsonObject("duration").get("text").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching delivery time. Please try again.";
        }
    }

    /**
     * Generates a Google Static Map URL with the route line.
     *
     * @param destination The delivery address.
     * @param width       Width of the map image.
     * @param height      Height of the map image.
     * @param scale       Scale factor for higher resolution.
     * @return Static map URL with route path.
     */
    public String getStaticMapURL(String destination, int width, int height, int scale) {
        String formattedDestination = formatAddress(destination);
        String encodedPolyline = getEncodedPolyline(formattedDestination);

        if (encodedPolyline.isEmpty()) {
            System.err.println("Error: Unable to fetch polyline for route.");
            return "";
        }

        return STATIC_MAP_BASE_URL +
                "?size=" + width + "x" + height +
                "&scale=" + scale +
                "&maptype=roadmap" +
                "&markers=color:red|label:S|" + SHOP_LOCATION +
                "&markers=color:blue|label:D|" + formattedDestination +
                "&path=enc:" + encodedPolyline +
                "&key=" + API_KEY;
    }

    /**
     * Fetches the encoded polyline for the route.
     *
     * @param destination The delivery address.
     * @return Encoded polyline string, or empty on failure.
     */
    private String getEncodedPolyline(String destination) {
        String url = buildDirectionsURL(destination);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(url))) {

            JsonObject json = parseResponse(response);

            return json.getAsJsonArray("routes").get(0)
                    .getAsJsonObject().get("overview_polyline")
                    .getAsJsonObject().get("points").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Parses the HTTP response into a JSON object.
     *
     * @param response HTTP response.
     * @return Parsed JsonObject.
     * @throws Exception If parsing fails.
     */
    private JsonObject parseResponse(CloseableHttpResponse response) throws Exception {
        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    /**
     * Formats the address for Google Maps API compatibility.
     *
     * @param address The original address.
     * @return Formatted address with '+' replacing spaces.
     */
    private String formatAddress(String address) {
        return address.trim().replace(" ", "+");
    }

    /**
     * Builds the Directions API request URL.
     *
     * @param destination The delivery destination.
     * @return Constructed URL for the Directions API.
     */
    private String buildDirectionsURL(String destination) {
        return DIRECTIONS_API_BASE_URL + "?origin=" + SHOP_LOCATION +
                "&destination=" + destination + "&key=" + API_KEY;
    }
}
