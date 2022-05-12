package persistence;

import model.Client;
import model.Product;
import model.Stock;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads stock from JSON data stored in file
// Inspired by the JsonWriter provided in the Serialization Demo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads stock from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Stock read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseStock(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses stock from JSON object and returns it
    private Stock parseStock(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Stock stock = new Stock(name);
        addProducts(stock, jsonObject);
        return stock;
    }

    // MODIFIES: stock
    // EFFECTS: parses products or clients from JSON object and adds them to workroom
    private void addProducts(Stock stock, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        for (Object json : jsonArray) {
            JSONObject nextProduct = (JSONObject) json;
            addProduct(stock, nextProduct);
        }
    }

    // MODIFIES: stock
    // EFFECTS: parses product from JSON object and adds it to workroom
    private void addProduct(Stock stock, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int price = jsonObject.getInt("price");
        int number = jsonObject.getInt("number");
        String clientName = jsonObject.getString("client");
        stock.addProductToStock(name, price, number, clientName);
    }
}

