package id.ac.ui.cs.advprog.eshop.repository;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentRepositoryTest {
  private PaymentRepository paymentRepository;
  private List<Payment> payments;

  @BeforeEach
  void setUp() {
    paymentRepository = new PaymentRepository();

    List<Product> products = new ArrayList<>();
    Product product = new Product();
    product.setProductId("product-1");
    product.setProductName("Sampo Cap Bambang");
    product.setProductQuantity(2);
    products.add(product);

    Order firstOrder = new Order("order-1", products, 1708560000L, "Marco");
    Order secondOrder = new Order("order-2", products, 1708560001L, "Marco");
    Order thirdOrder = new Order("order-3", products, 1708560002L, "Bambang");

    Map<String, String> firstPaymentData = new HashMap<>();
    firstPaymentData.put("voucherCode", "DISKON50");

    Map<String, String> secondPaymentData = new HashMap<>();
    secondPaymentData.put("voucherCode", "HEMAT10");

    Map<String, String> thirdPaymentData = new HashMap<>();
    thirdPaymentData.put("voucherCode", "ONGKIR0");

    payments = new ArrayList<>();
    payments.add(new Payment("payment-1", firstOrder, "Voucher Code", "PENDING", firstPaymentData));
    payments.add(
        new Payment("payment-2", secondOrder, "Voucher Code", "SUCCESS", secondPaymentData));
    payments.add(
        new Payment("payment-3", thirdOrder, "Voucher Code", "REJECTED", thirdPaymentData));
  }

  @Test
  void testSaveCreate() {
    Payment payment = payments.get(0);

    Payment result = paymentRepository.save(payment);
    Payment findResult = paymentRepository.findById(payment.getId());

    assertEquals(payment.getId(), result.getId());
    assertEquals(payment.getId(), findResult.getId());
    assertSame(payment.getOrder(), findResult.getOrder());
    assertEquals(payment.getMethod(), findResult.getMethod());
    assertEquals(payment.getStatus(), findResult.getStatus());
    assertEquals(payment.getPaymentData(), findResult.getPaymentData());
  }

  @Test
  void testSaveUpdate() {
    Payment originalPayment = payments.get(0);
    paymentRepository.save(originalPayment);

    Map<String, String> updatedPaymentData = new HashMap<>();
    updatedPaymentData.put("voucherCode", "DISKON75");
    Payment updatedPayment =
        new Payment(
            originalPayment.getId(),
            originalPayment.getOrder(),
            originalPayment.getMethod(),
            "SUCCESS",
            updatedPaymentData);

    Payment result = paymentRepository.save(updatedPayment);
    Payment findResult = paymentRepository.findById(originalPayment.getId());

    assertEquals(updatedPayment.getId(), result.getId());
    assertEquals(updatedPayment.getId(), findResult.getId());
    assertSame(updatedPayment.getOrder(), findResult.getOrder());
    assertEquals("SUCCESS", findResult.getStatus());
    assertEquals("DISKON75", findResult.getPaymentData().get("voucherCode"));
  }

  @Test
  void testFindByIdIfIdFound() {
    for (Payment payment : payments) {
      paymentRepository.save(payment);
    }

    Payment findResult = paymentRepository.findById(payments.get(1).getId());

    assertEquals(payments.get(1).getId(), findResult.getId());
    assertSame(payments.get(1).getOrder(), findResult.getOrder());
    assertEquals(payments.get(1).getMethod(), findResult.getMethod());
    assertEquals(payments.get(1).getStatus(), findResult.getStatus());
  }

  @Test
  void testFindByIdIfIdNotFound() {
    for (Payment payment : payments) {
      paymentRepository.save(payment);
    }

    Payment findResult = paymentRepository.findById("missing-id");

    assertNull(findResult);
  }

  @Test
  void testFindAllByAuthorIfAuthorCorrect() {
    for (Payment payment : payments) {
      paymentRepository.save(payment);
    }

    List<Payment> paymentList = paymentRepository.findAllByAuthor("Marco");

    assertEquals(2, paymentList.size());
    assertEquals("payment-1", paymentList.get(0).getId());
    assertEquals("payment-2", paymentList.get(1).getId());
  }

  @Test
  void testFindAllByAuthorIfAllLowercase() {
    for (Payment payment : payments) {
      paymentRepository.save(payment);
    }

    List<Payment> paymentList = paymentRepository.findAllByAuthor("marco");

    assertTrue(paymentList.isEmpty());
  }
}
