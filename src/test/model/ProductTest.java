package model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product testProduct;
    Client testClient;

    @BeforeEach
    void runBefore() {
        testClient = new Client("Test Client", new ArrayList<>());
        testProduct = new Product("Test Product", 1500, 1, testClient);
    }

    @Test
    void testConstructor() {
        assertEquals("Test Product", testProduct.getName());
        assertEquals(1500, testProduct.getPrice());
        assertEquals(1, testProduct.getNumber());
        assertEquals(1500, testProduct.getValue());
        assertEquals(testClient, testProduct.getClient());
    }

    @Test
    void testSetName() {
        assertEquals("Test Product", testProduct.getName());
        testProduct.setName("New Product");
        assertEquals("New Product", testProduct.getName());
    }

    @Test
    void testSetPrice() {
        assertEquals(1500, testProduct.getPrice());
        testProduct.setPrice(2500);
        assertEquals(2500, testProduct.getPrice());
    }

    @Test
    void testChangeNumber() {
        assertEquals(1, testProduct.getNumber());
        testProduct.addNumber(8);
        assertEquals(9, testProduct.getNumber());
        testProduct.removeNumber(3);
        assertEquals(6, testProduct.getNumber());
    }

    @Test
    void testGetValue() {
        testProduct.addNumber(8);
        assertEquals(13500, testProduct.getValue());
        testProduct.removeNumber(3);
        assertEquals(9000, testProduct.getValue());
    }

    @Test
    void testIsEqual() {
        assertTrue(testProduct.isEqual(testProduct));
        assertTrue(testProduct.isEqual(new Product("Test Product", 1500, 1, testClient)));
        assertFalse(testProduct.isEqual(new Product("Test Product", 2000, 1, testClient)));
    }
}