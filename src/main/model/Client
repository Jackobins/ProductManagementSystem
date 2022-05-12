package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Represents a client with a name, and a list of products that they wish to buy
public class Client {
    private String name;
    private ArrayList<Product> products;

    public Client(String name, ArrayList<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    // MODIFIES: this
    // EFFECTS: Adds a product to the client's list of products
    public void addProduct(Product product) {
        this.products.add(product);
    }

    // MODIFIES: this
    // EFFECTS: Removes a product from the client's list of products
    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    // EFFECTS: Returns the first matching client with a given client and list of clients
    //          (matched by name)
    public Client getMatchingClient(List<Client> clientList) {
        Client output = this;
        for (Client client : clientList) {
            if (client.getName().equals(this.getName())) {
                output = client;
            }
        }
        return output;
    }
}

