package id.ac.ui.cs.advprog.eshop.repository;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.Iterator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

// import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

  @InjectMocks ProductRepository productRepository;

  @BeforeEach
  void setUp() {}

  @Test
  void testCreateAndFind() {
    Product product = new Product();
    product.setProductId("id1");
    product.setProductName("Sampo Cap Bambang");
    product.setProductQuantity(100);
    productRepository.create(product);

    Iterator<Product> productIterator = productRepository.findAll();
    assertTrue(productIterator.hasNext());
    Product savedProduct = productIterator.next();
    assertEquals(product.getProductId(), savedProduct.getProductId());
    assertEquals(product.getProductName(), savedProduct.getProductName());
    assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
  }

  @Test
  void testFindAllIfEmpty() {
    Iterator<Product> productIterator = productRepository.findAll();
    assertFalse(productIterator.hasNext());
  }

  @Test
  void testFindAllIfMoreThanOneProduct() {
    Product product1 = new Product();
    product1.setProductId("id1");
    product1.setProductName("Sampo Cap Bambang");
    product1.setProductQuantity(100);
    productRepository.create(product1);

    Product product2 = new Product();
    product2.setProductId("id2");
    product2.setProductName("Sampo Cap Usep");
    product2.setProductQuantity(50);
    productRepository.create(product2);

    Iterator<Product> productIterator = productRepository.findAll();
    assertTrue(productIterator.hasNext());
    Product savedProduct = productIterator.next();
    assertEquals(product1.getProductId(), savedProduct.getProductId());
    assertTrue(productIterator.hasNext());
    savedProduct = productIterator.next();
    assertEquals(product2.getProductId(), savedProduct.getProductId());
    assertFalse(productIterator.hasNext());
  }

  @Test
  void findByIdReturnsProductWhenPresent() {
    Product product = new Product();
    product.setProductId("id1");
    product.setProductName("Found");
    product.setProductQuantity(10);
    productRepository.create(product);

    Optional<Product> result = productRepository.findById("id1");

    assertTrue(result.isPresent());
    assertEquals(product, result.get());
  }

  @Test
  void findByIdReturnsEmptyWhenIdIsNullOrMissing() {
    productRepository.create(new Product());

    assertFalse(productRepository.findById(null).isPresent());
    assertFalse(productRepository.findById("missing").isPresent());
  }

  @Test
  void updateReturnsNullWhenInputInvalid() {
    assertNull(productRepository.update(null));

    Product noId = new Product();
    assertNull(productRepository.update(noId));
  }

  @Test
  void updateReturnsNullWhenProductNotFound() {
    Product existing = new Product();
    existing.setProductId("id1");
    existing.setProductName("Old");
    existing.setProductQuantity(1);
    productRepository.create(existing);

    Product unknown = new Product();
    unknown.setProductId("id2");
    unknown.setProductName("New");
    unknown.setProductQuantity(2);

    assertNull(productRepository.update(unknown));
  }

  @Test
  void updateMutatesMatchingProduct() {
    Product existing = new Product();
    existing.setProductId("id1");
    existing.setProductName("Old");
    existing.setProductQuantity(1);
    productRepository.create(existing);

    Product updated = new Product();
    updated.setProductId("id1");
    updated.setProductName("New Name");
    updated.setProductQuantity(5);

    Product result = productRepository.update(updated);

    assertNotNull(result);
    assertEquals("New Name", result.getProductName());
    assertEquals(5, result.getProductQuantity());
    assertEquals(existing, result);
  }

  @Test
  void deleteByIdHandlesNullAndMissingId() {
    assertFalse(productRepository.deleteById(null));
    assertFalse(productRepository.deleteById("missing"));
  }

  @Test
  void deleteByIdRemovesOnlyMatchingProduct() {
    Product first = new Product();
    first.setProductId("id1");
    Product second = new Product();
    second.setProductId("id2");
    productRepository.create(first);
    productRepository.create(second);

    assertTrue(productRepository.deleteById("id1"));

    Iterator<Product> remaining = productRepository.findAll();
    assertTrue(remaining.hasNext());
    assertEquals("id2", remaining.next().getProductId());
    assertFalse(remaining.hasNext());
  }
}
