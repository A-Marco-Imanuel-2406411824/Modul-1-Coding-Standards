package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EditProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void findByIdReturnsProductWhenPresent() {
        Product product = new Product();
        product.setProductId("prod-1");
        product.setProductName("Shampoo");
        product.setProductQuantity(10);
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findById("prod-1");

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository).findById("prod-1");
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<Product> result = productService.findById("missing");

        assertFalse(result.isPresent());
        verify(productRepository).findById("missing");
    }

    @Test
    void updateDelegatesToRepository() {
        Product product = new Product();
        product.setProductId("prod-2");
        product.setProductName("Soap");
        product.setProductQuantity(5);
        when(productRepository.update(product)).thenReturn(product);

        Product updated = productService.update(product);

        assertEquals(product, updated);
        verify(productRepository).update(product);
    }
}
