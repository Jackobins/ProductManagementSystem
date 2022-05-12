package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a product with a name, a price, the number of
// the products that are in the stock and the client that wants it
public class Product implements Writable {
    private String name;
    private int price;
    private int number;
    private Client client;

    // REQUIRES: price > 0, number > 0
    // EFFECTS: Creates a given number of the same
    //          product with a given name and price in $
    public Product(String name, int price, int number, Client client) {
        this.name = name;
        this.price = price;
        this.number = number;
        this.client = client;
        client.addProduct(this);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public Client getClient() {
        return client;
    }

    // EFFECTS: Returns the total value of the product in stock
    //          (the price of one product multiplied by the number of products)
    public int getValue() {
        return price * number;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setPrice(int newPrice) {
        this.price = newPrice;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: Adds a given amount of the product
    public void addNumber(int amount) {
        this.number += amount;
    }

    // REQUIRES: amount > 0, number-amount >= 0
    // MODIFIES: this
    // EFFECTS: Removes a given amount of the product
    public void removeNumber(int amount) {
        this.number -= amount;
        if (this.number == 0) {
            this.client.removeProduct(this);
        }
    }

    // EFFECTS: Returns whether two products are equal (without number)
    public boolean isEqual(Product newProduct) {
        return this.getName().equals(newProduct.getName())
                && this.price == newProduct.getPrice()
                && this.client.getName().equals(newProduct.getClient().getName());
    }

    @Override
    // Inspired by the code provided in the Serialization Demo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("price", price);
        json.put("number", number);
        json.put("client", client.getName());
        return json;
    }
}
