package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void deleteExistingProductReturnsTrue() {
        String id = "prod-1";
        when(productRepository.deleteById(id)).thenReturn(true);

        boolean deleted = productService.deleteById(id);

        assertTrue(deleted);
        verify(productRepository).deleteById(id);
    }

    @Test
    void deleteMissingProductReturnsFalse() {
        String id = "missing";
        when(productRepository.deleteById(id)).thenReturn(false);

        boolean deleted = productService.deleteById(id);

        assertFalse(deleted);
        verify(productRepository).deleteById(id);
    }
}
