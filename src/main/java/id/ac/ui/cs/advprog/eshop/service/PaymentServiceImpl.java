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
  private static final String VOUCHER_CODE_METHOD = "Voucher Code";
  private static final String VOUCHER_CODE_KEY = "voucherCode";
  private static final String VOUCHER_PREFIX = "ESHOP";
  private static final int VOUCHER_LENGTH = 16;
  private static final int REQUIRED_DIGIT_COUNT = 8;

  @Autowired private PaymentRepository paymentRepository;

  @Override
  public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
    PaymentStatus initialStatus = resolveInitialStatus(method, paymentData);
    Payment payment = new Payment(
        UUID.randomUUID().toString(),
        order,
        method,
        initialStatus.getValue(),
        paymentData);
    return paymentRepository.save(payment);
  }

  @Override
  public Payment setStatus(Payment payment, String status) {
    payment.setStatus(status);
    final boolean isSuccess = PaymentStatus.SUCCESS.getValue().equals(status);
    final boolean isRejected = PaymentStatus.REJECTED.getValue().equals(status);

    if (isSuccess) {
      payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
    } else if (isRejected) {
      payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
    }
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

  private PaymentStatus resolveInitialStatus(String method, Map<String, String> paymentData) {
    if (!VOUCHER_CODE_METHOD.equals(method)) {
      return PaymentStatus.PENDING;
    }

    if (isValidVoucherCode(paymentData)) {
      return PaymentStatus.SUCCESS;
    }
    return PaymentStatus.REJECTED;
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
