package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a stock; a list of products that are in stock, as well as
// a list of customers who are wanting to buy those products
public class Stock implements Writable {
    private List<Product> products;
    private List<Client> clients;
    private String name;

    // EFFECTS: Creates a representation of a stock with
    //          a list of products and a list of clients
    //          that wish to purchase from the company
    public Stock(String name) {
        this.name = name;
        this.products = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: Adds a product of the given specifications to the existing stock
    public void addProductToStock(String productName, int productPrice, int amount, String clientName) {
        Client newClient = new Client(clientName, new ArrayList<>());
        Product newProduct = new Product(productName, productPrice, amount, newClient);

        if (!checkIfClientAlreadyExists(this.clients, newProduct.getClient())) {
            this.clients.add(newProduct.getClient());
        }

        if (!checkIfProductAlreadyExists(this.products, newProduct)) {
            this.products.add(newProduct);
        } else {
            for (Product product : this.products) {
                if (product.isEqual(newProduct)) {
                    product.addNumber(newProduct.getNumber());
                }
            }
        }

        if (!checkIfProductAlreadyExists(newClient.getMatchingClient(this.clients).getProducts(), newProduct)) {
            newClient.getMatchingClient(this.clients).addProduct(newProduct);
        }

        EventLog.getInstance().logEvent(new Event(productName + " x" + amount + " added to stock."));
    }

    // EFFECTS: Checks if the product is already in the stock's list of products
    //          (identified by name, price and client name only)
    private boolean checkIfProductAlreadyExists(List<Product> productList, Product newProduct) {
        boolean output = false;
        for (Product product : productList) {
            if (product.isEqual(newProduct)) {
                output = true;
                break;
            }
        }
        return output;
    }

    // EFFECTS: Checks if the client is already in the stock's list of clients
    //          (identified by name only)
    private boolean checkIfClientAlreadyExists(List<Client> clientList, Client newClient) {
        boolean output = false;
        for (Client client : clientList) {
            if (client.getName().equals(newClient.getName())) {
                output = true;
                break;
            }
        }
        return output;
    }

    // REQUIRES: amount > 0, product must be in stock, product number - amount >= 0
    // MODIFIES: this
    // EFFECTS: removes a given amount of a certain product that belongs to a given client from the stock
    public void removeProductFromStock(String productName, int productPrice, String clientName, int amount) {
        Client newClient = new Client(clientName, new ArrayList<>());
        Product productToRemove = new Product(productName, productPrice, amount, newClient);
        for (Product product : this.products) {
            if (product.isEqual(productToRemove)) {
                Client productClient = product.getClient();
                product.removeNumber(amount);
                if (product.getNumber() == 0) {
                    this.products.remove(product);
                }
                if (productClient.getProducts().isEmpty()) {
                    this.clients.remove(productClient);
                }
                break;
            }
        }

        EventLog.getInstance().logEvent(new Event(productName + " x" + amount + " removed from stock."));
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Client> getClients() {
        return clients;
    }

    public String getName() {
        return name;
    }

    // EFFECTS: Returns all products as a list of strings (product names)
    public ArrayList<String> getAllProductsString() {
        ArrayList<String> output = new ArrayList<>();
        for (Product product : this.products) {
            output.add(product.getName());
        }
        return output;
    }

    // EFFECTS: Returns all clients as a list of strings (client names)
    public ArrayList<String> getAllClientsString() {
        ArrayList<String> output = new ArrayList<>();
        for (Client client : this.clients) {
            output.add(client.getName());
        }
        return output;
    }

    // EFFECTS: Returns all products and their fields in string form
    public String getProductsDataString() {
        String output = "";
        for (Product product : this.products) {
            output = output + product.getName() + ": $" + product.getPrice()
                    + " x" + product.getNumber() + ", " + product.getClient().getName() + "\n";
        }
        return output;
    }

    // EFFECTS: Returns all clients and their fields in string form
    public String getClientsDataString() {
        String output = "";
        for (Client client : this.clients) {
            output = output + getSpecificClient(client.getName()) + "\n";
        }
        return output;
    }

    // REQUIRES: Client must have ordered a product
    // EFFECTS: Returns all the products that the client has ordered in string form
    public String getSpecificClient(String clientName) {
        String output = clientName + ":";
        for (Client client : this.clients) {
            if (client.getName().equals(clientName)) {
                for (Product product : client.getProducts()) {
                    output = output + " " + product.getName();
                }
                break;
            }
        }
        return output;
    }

    // REQUIRES: The product is in the stock
    // EFFECTS: Returns all the clients that have ordered this product in string form
    public String getSpecificProduct(String productName) {
        String output = productName + ":";
        for (Product product : this.products) {
            if (product.getName().equals(productName)) {
                output = output + " " + product.getClient().getName();
            }
        }
        return output;
    }

    // EFFECTS: Returns the total value of the stock (in $);
    //          the sum of the values of all the products
    public int getTotalStockValue() {
        int totalValue = 0;
        for (Product product : this.products) {
            totalValue += product.getValue();
        }
        return totalValue;
    }

    // REQUIRES: newPrice >= 0, product must be in stock
    // MODIFIES: this
    // EFFECTS: Changes the price of all the products with the given name
    //          to the given amount (in $)
    public void changePrices(String productName, int newPrice) {
        for (Product product : this.products) {
            if (product.getName().equals(productName)) {
                product.setPrice(newPrice);
            }
        }
    }

    @Override
    // Inspired by the code provided in the Serialization Demo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("products", productsAndClientsToJson());
        return json;
    }

    // EFFECTS: returns products or clients in this stock as a JSON array
    // Inspired by the code provided in the Serialization Demo
    private JSONArray productsAndClientsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Product product : this.products) {
            jsonArray.put(product.toJson());
        }
        return jsonArray;
    }
}
