package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Inspired by JSON Serialization Demo
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Stock stock = new Stock("My Stock");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyStock() {
        try {
            Stock stock = new Stock("My Stock");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyStock.json");
            writer.open();
            writer.write(stock);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyStock.json");
            stock = reader.read();
            assertEquals("My Stock", stock.getName());
            assertTrue(stock.getProducts().isEmpty());
            assertTrue(stock.getClients().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStock() {
        try {
            Stock stock = new Stock("My Stock");
            stock.addProductToStock("Camera", 200, 2, "Jack");
            stock.addProductToStock("Laptop", 1500, 1, "Bob");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralStock.json");
            writer.open();
            writer.write(stock);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralStock.json");
            stock = reader.read();
            assertEquals("My Stock", stock.getName());
            List<Product> products = stock.getProducts();
            List<Client> clients = stock.getClients();
            assertEquals(2, products.size());
            assertEquals(2, clients.size());
            assertEquals("Camera: $200 x2, Jack\nLaptop: $1500 x1, Bob\n",
                    stock.getProductsDataString());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
