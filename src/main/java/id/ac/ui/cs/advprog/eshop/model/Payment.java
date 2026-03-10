package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import java.util.Map;
import lombok.Getter;

@Getter
public class Payment {
  private final String id;
  private final Order order;
  private final String method;
  private PaymentStatus status;
  private final Map<String, String> paymentData;

  public Payment(String id, Order order, String method, String status,
      Map<String, String> paymentData) {
    this.id = id;
    this.order = order;
    this.method = method;
    this.paymentData = paymentData;
    setStatus(status);
  }

  public String getStatus() {
    return status.getValue();
  }

  public void setStatus(String status) {
    this.status = PaymentStatus.fromString(status);
  }
}
