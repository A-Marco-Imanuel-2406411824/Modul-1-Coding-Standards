package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
  // Valued constants for payment by voucher code sub feature
  private static final String VOUCHER_CODE_METHOD = "Voucher Code";
  private static final String VOUCHER_CODE_KEY = "voucherCode";
  private static final String VOUCHER_PREFIX = "ESHOP";
  private static final int VOUCHER_LENGTH = 16;
  private static final int REQUIRED_DIGIT_COUNT = 8;

  // Valued constants for payment by bank transfer sub feature
  private static final String BANK_TRANSFER_METHOD = "Bank Transfer";
  private static final String BANK_NAME_KEY = "bankName";
  private static final String REFERENCE_CODE_KEY = "referenceCode";

  @Autowired private PaymentRepository paymentRepository;

  @Override
  public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
    Payment payment = createPayment(order, method, paymentData);
    return paymentRepository.save(payment);
  }

  @Override
  public Payment setStatus(Payment payment, String status) {
    PaymentStatus paymentStatus = PaymentStatus.fromString(status);
    payment.setStatus(paymentStatus.getValue());
    updateOrderStatus(payment.getOrder(), paymentStatus);
    return paymentRepository.save(payment);
  }

  @Override
  public Payment getPayment(String paymentId) {
    return paymentRepository.findById(paymentId);
  }

  @Override
  public List<Payment> getAllPayments(String author) {
    return paymentRepository.findAllByAuthor(author);
  }

  // Private helpers
  private Payment createPayment(Order order, String method, Map<String, String> paymentData) {
    PaymentStatus initialStatus = determineInitialStatus(method, paymentData);
    return new Payment(
        UUID.randomUUID().toString(), order, method, initialStatus.getValue(), paymentData);
  }

  private PaymentStatus determineInitialStatus(String method, Map<String, String> paymentData) {
    if (isVoucherCodeMethod(method)) {
      return isValidVoucherCode(paymentData) ? PaymentStatus.SUCCESS : PaymentStatus.REJECTED;
    }

    if (isBankTransferMethod(method)) {
      return isValidBankTransfer(paymentData) ? PaymentStatus.PENDING : PaymentStatus.REJECTED;
    }

    return PaymentStatus.PENDING;
  }

  private boolean isVoucherCodeMethod(String method) {
    return VOUCHER_CODE_METHOD.equals(method);
  }

  private boolean isBankTransferMethod(String method) {
    return BANK_TRANSFER_METHOD.equals(method);
  }

  private void updateOrderStatus(Order order, PaymentStatus paymentStatus) {
    if (paymentStatus == PaymentStatus.SUCCESS) {
      order.setStatus(OrderStatus.SUCCESS.getValue());
    } else if (paymentStatus == PaymentStatus.REJECTED) {
      order.setStatus(OrderStatus.FAILED.getValue());
    }
  }

  private boolean isValidVoucherCode(Map<String, String> paymentData) {
    if (paymentData == null) {
      return false;
    }

    String voucherCode = paymentData.get(VOUCHER_CODE_KEY);
    if (voucherCode == null) {
      return false;
    }

    boolean hasValidLength = voucherCode.length() == VOUCHER_LENGTH;
    boolean hasValidPrefix = voucherCode.startsWith(VOUCHER_PREFIX);
    boolean hasRequiredDigits = countDigits(voucherCode) == REQUIRED_DIGIT_COUNT;

    return hasValidLength && hasValidPrefix && hasRequiredDigits;
  }

  private boolean isValidBankTransfer(Map<String, String> paymentData) {
    if (paymentData == null) {
      return false;
    }

    return hasNonEmptyValue(paymentData, BANK_NAME_KEY)
        && hasNonEmptyValue(paymentData, REFERENCE_CODE_KEY);
  }

  private boolean hasNonEmptyValue(Map<String, String> paymentData, String key) {
    String value = paymentData.get(key);
    return value != null && !value.isEmpty();
  }

  private int countDigits(String value) {
    int digitCount = 0;
    for (char currentChar : value.toCharArray()) {
      if (Character.isDigit(currentChar)) {
        digitCount += 1;
      }
    }
    return digitCount;
  }
}
