package id.ac.ui.cs.advprog.eshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock private ProductRepository productRepository;

  @InjectMocks private ProductServiceImpl productService;

  @Test
  void createGeneratesIdWhenMissing() {
    Product product = new Product();
    when(productRepository.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Product created = productService.create(product);

    assertNotNull(created.getProductId());
    assertFalse(created.getProductId().isBlank());
    verify(productRepository).create(product);
  }

  @Test
  void createKeepsExistingId() {
    Product product = new Product();
    product.setProductId("existing-id");
    when(productRepository.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Product created = productService.create(product);

    assertEquals("existing-id", created.getProductId());
    verify(productRepository).create(product);
  }

  @Test
  void findAllAggregatesIteratorIntoList() {
    Product first = createProduct("id-1", "Item A", 1);
    Product second = createProduct("id-2", "Item B", 2);
    Iterator<Product> iterator = Arrays.asList(first, second).iterator();
    when(productRepository.findAll()).thenReturn(iterator);

    var result = productService.findAll();

    assertEquals(2, result.size());
    assertEquals(first, result.get(0));
    assertEquals(second, result.get(1));
    verify(productRepository).findAll();
  }

  @Test
  void findAllReturnsEmptyListWhenNoData() {
    when(productRepository.findAll()).thenReturn(Arrays.<Product>asList().iterator());

    var result = productService.findAll();

    assertTrue(result.isEmpty());
    verify(productRepository).findAll();
  }

  private Product createProduct(String id, String name, int qty) {
    Product product = new Product();
    product.setProductId(id);
    product.setProductName(name);
    product.setProductQuantity(qty);
    return product;
  }
}

