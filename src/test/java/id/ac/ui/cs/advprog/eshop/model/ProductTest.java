package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {
  Product product;

  @BeforeEach
  void setUp() {
    this.product = new Product();
    this.product.setProductId("id1");
    this.product.setProductName("Sampo Cap Bambang");
    this.product.setProductQuantity(100);
  }

  @Test
  void testGetProductId() {
    assertEquals("id1", this.product.getProductId());
  }

  @Test
  void testGetProductName() {
    assertEquals("Sampo Cap Bambang", this.product.getProductName());
  }

  @Test
  void testGetProductQuantity() {
    assertEquals(100, this.product.getProductQuantity());
  }

  @Test
  void testSettersUpdateFields() {
    product.setProductId("new-id");
    product.setProductName("Updated Name");
    product.setProductQuantity(-5);

    assertEquals("new-id", product.getProductId());
    assertEquals("Updated Name", product.getProductName());
    assertEquals(-5, product.getProductQuantity());
  }
}
