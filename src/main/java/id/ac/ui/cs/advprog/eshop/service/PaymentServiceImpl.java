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
  @Autowired private PaymentRepository paymentRepository;

  @Override
  public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
    Payment payment = new Payment(
        UUID.randomUUID().toString(),
        order,
        method,
        PaymentStatus.PENDING.getValue(),
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
}
