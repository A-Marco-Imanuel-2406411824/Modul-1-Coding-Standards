package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTest {
  private Order order;
  private Map<String, String> paymentData;

  @BeforeEach
  void setUp() {
    List<Product> products = new ArrayList<>();

    Product product = new Product();
    product.setProductId("product-1");
    product.setProductName("Sampo Cap Bambang");
    product.setProductQuantity(2);
    products.add(product);

    order = new Order("order-1", products, 1708560000L, "Marco");

    paymentData = new HashMap<>();
    paymentData.put("voucherCode", "DISKON50");
    paymentData.put("address", "Depok");
  }

  @Test
  void testCreatePaymentStoresAllFields() {
    Payment payment = new Payment("payment-1", order, "Voucher Code", "PENDING", paymentData);

    assertEquals("payment-1", payment.getId());
    assertSame(order, payment.getOrder());
    assertEquals("Voucher Code", payment.getMethod());
    assertEquals("PENDING", payment.getStatus());
    assertEquals("DISKON50", payment.getPaymentData().get("voucherCode"));
    assertEquals("Depok", payment.getPaymentData().get("address"));
  }

  @Test
  void testCreatePaymentWithSuccessStatusDoesNotChangeOrderStatus() {
    Payment payment = new Payment("payment-1", order, "Voucher Code", "SUCCESS", paymentData);

    assertEquals("SUCCESS", payment.getStatus());
    assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
  }

  @Test
  void testSetStatusSuccessUpdatesPaymentStatus() {
    Payment payment = new Payment("payment-1", order, "Voucher Code", "PENDING", paymentData);

    payment.setStatus("SUCCESS");

    assertEquals("SUCCESS", payment.getStatus());
  }

  @Test
  void testSetStatusRejectedUpdatesPaymentStatus() {
    Payment payment = new Payment("payment-1", order, "Voucher Code", "PENDING", paymentData);

    payment.setStatus("REJECTED");

    assertEquals("REJECTED", payment.getStatus());
  }

  @Test
  void testSetStatusInvalidThrowsException() {
    Payment payment = new Payment("payment-1", order, "Voucher Code", "PENDING", paymentData);

    assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
  }
}

