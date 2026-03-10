package id.ac.ui.cs.advprog.eshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  @InjectMocks PaymentServiceImpl paymentService;

  @Mock PaymentRepository paymentRepository;

  private Order order;
  private Payment payment;
  private Map<String, String> paymentData;
  private List<Payment> payments;

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
    paymentData.put("voucherCode", "ESHOP1234ABC5678");
    paymentData.put("address", "Depok");

    payment = new Payment("payment-1", order, "Voucher Code", "PENDING", paymentData);

    payments = new ArrayList<>();
    payments.add(payment);
    payments.add(new Payment("payment-2", order, "Voucher Code", "SUCCESS", paymentData));
  }

  @Test
  void testAddPaymentWithValidVoucherCodeSetsStatusToSuccess() {
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Voucher Code", paymentData);

    verify(paymentRepository, times(1)).save(any(Payment.class));
    assertEquals(order, result.getOrder());
    assertEquals("Voucher Code", result.getMethod());
    assertEquals("SUCCESS", result.getStatus());
    assertEquals(paymentData, result.getPaymentData());
  }

  @Test
  void testAddPaymentWithVoucherCodeWrongLengthSetsStatusToRejected() {
    Map<String, String> invalidPaymentData = new HashMap<>();
    invalidPaymentData.put("voucherCode", "ESHOP1234ABC567");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Voucher Code", invalidPaymentData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithVoucherCodeWrongPrefixSetsStatusToRejected() {
    Map<String, String> invalidPaymentData = new HashMap<>();
    invalidPaymentData.put("voucherCode", "TOKO1234ABC5678");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Voucher Code", invalidPaymentData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithVoucherCodeWithoutEightDigitsSetsStatusToRejected() {
    Map<String, String> invalidPaymentData = new HashMap<>();
    invalidPaymentData.put("voucherCode", "ESHOPABCDABCEFGHI");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Voucher Code", invalidPaymentData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithMissingVoucherCodeSetsStatusToRejected() {
    Map<String, String> invalidPaymentData = new HashMap<>();
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Voucher Code", invalidPaymentData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithValidBankTransferDefaultsToPending() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("bankName", "BCA");
    bankTransferData.put("referenceCode", "INV-001");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("PENDING", result.getStatus());
    assertEquals("Bank Transfer", result.getMethod());
    assertEquals(bankTransferData, result.getPaymentData());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithMissingBankNameSetsStatusToRejected() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("referenceCode", "INV-001");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithNullBankNameSetsStatusToRejected() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("bankName", null);
    bankTransferData.put("referenceCode", "INV-001");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithEmptyBankNameSetsStatusToRejected() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("bankName", "");
    bankTransferData.put("referenceCode", "INV-001");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithMissingReferenceCodeSetsStatusToRejected() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("bankName", "BCA");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithNullReferenceCodeSetsStatusToRejected() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("bankName", "BCA");
    bankTransferData.put("referenceCode", null);
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testAddPaymentWithEmptyReferenceCodeSetsStatusToRejected() {
    Map<String, String> bankTransferData = new HashMap<>();
    bankTransferData.put("bankName", "BCA");
    bankTransferData.put("referenceCode", "");
    doAnswer(invocation -> invocation.getArgument(0))
        .when(paymentRepository)
        .save(any(Payment.class));

    Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferData);

    assertEquals("REJECTED", result.getStatus());
    verify(paymentRepository, times(1)).save(any(Payment.class));
  }

  @Test
  void testSetStatusToSuccessAlsoUpdatesOrderStatus() {
    doReturn(payment).when(paymentRepository).save(payment);

    Payment result = paymentService.setStatus(payment, "SUCCESS");

    verify(paymentRepository, times(1)).save(payment);
    assertEquals("SUCCESS", result.getStatus());
    assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
  }

  @Test
  void testSetStatusToRejectedAlsoUpdatesOrderStatusToFailed() {
    doReturn(payment).when(paymentRepository).save(payment);

    Payment result = paymentService.setStatus(payment, "REJECTED");

    verify(paymentRepository, times(1)).save(payment);
    assertEquals("REJECTED", result.getStatus());
    assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
  }

  @Test
  void testSetStatusInvalidThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(payment, "MEOW"));
    verify(paymentRepository, times(0)).save(any(Payment.class));
  }

  @Test
  void testGetPaymentIfIdFound() {
    doReturn(payment).when(paymentRepository).findById(payment.getId());

    Payment result = paymentService.getPayment(payment.getId());

    assertEquals(payment.getId(), result.getId());
    verify(paymentRepository, times(1)).findById(payment.getId());
  }

  @Test
  void testGetPaymentIfIdNotFound() {
    doReturn(null).when(paymentRepository).findById("missing-id");

    Payment result = paymentService.getPayment("missing-id");

    assertNull(result);
    verify(paymentRepository, times(1)).findById("missing-id");
  }

  @Test
  void testGetAllPaymentsByAuthor() {
    doReturn(payments).when(paymentRepository).findAllByAuthor(order.getAuthor());

    List<Payment> results = paymentService.getAllPayments(order.getAuthor());

    assertEquals(2, results.size());
    assertEquals(order.getAuthor(), results.get(0).getOrder().getAuthor());
    assertEquals(order.getAuthor(), results.get(1).getOrder().getAuthor());
    verify(paymentRepository, times(1)).findAllByAuthor(order.getAuthor());
  }
}
