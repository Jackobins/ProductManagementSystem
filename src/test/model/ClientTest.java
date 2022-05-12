package model;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    Client testClient;
    Stock testStock;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("Test Stock");
        testClient = new Client("Test Client", new ArrayList<>());
    }

    @Test
    void testConstructor() {
        assertEquals("Test Client", testClient.getName());
        assertEquals(new ArrayList<Product>(), testClient.getProducts());
    }

    @Test
    void testAddProduct() {
        ArrayList<Product> products = new ArrayList<>();
        Product testProduct1 = new Product("Test Product 1", 1500, 1, testClient);
        products.add(testProduct1);
        assertEquals(products, testClient.getProducts());

        Product testProduct2 = new Product("Test Product 2", 2000, 2, testClient);
        products.add(testProduct2);
        assertEquals(products, testClient.getProducts());
    }

    @Test
    void testRemoveProductAboveZero() {
        ArrayList<Product> products = new ArrayList<>();
        Product testProduct1 = new Product("Test Product 1", 1500, 5, testClient);
        products.add(testProduct1);
        testProduct1.removeNumber(3);
        assertEquals(products, testClient.getProducts());
    }

    @Test
    void testRemoveProductToZero() {
        ArrayList<Product> products = new ArrayList<>();
        Product testProduct1 = new Product("Test Product 1", 1500, 5, testClient);
        testProduct1.removeNumber(5);
        assertEquals(products, testClient.getProducts());
    }

    @Test
    void testRemoveMultipleProductToZero(){
        ArrayList<Product> products = new ArrayList<>();
        Product testProduct1 = new Product("Test Product 1", 1500, 5, testClient);
        Product testProduct2 = new Product("Test Product 2", 1300, 2, testClient);
        Product testProduct3 = new Product("Test Product 3", 100, 3, testClient);
        products.add(testProduct1);
        products.add(testProduct2);
        products.add(testProduct3);
        assertEquals(products, testClient.getProducts());

        testProduct2.removeNumber(2);
        products.remove(testProduct2);
        assertEquals(products, testClient.getProducts());

        testProduct1.removeNumber(5);
        products.remove(testProduct1);
        assertEquals(products, testClient.getProducts());
    }

}
