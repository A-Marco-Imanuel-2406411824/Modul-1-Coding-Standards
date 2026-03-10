package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
  PENDING("PENDING"),
  SUCCESS("SUCCESS"),
  REJECTED("REJECTED");

  private final String value;

  PaymentStatus(String value) {
    this.value = value;
  }

  public static boolean contains(String param) {
    for (PaymentStatus paymentStatus : PaymentStatus.values()) {
      if (paymentStatus.name().equals(param)) {
        return true;
      }
    }
    return false;
  }

  public static PaymentStatus fromString(String status) {
    for (PaymentStatus paymentStatus : PaymentStatus.values()) {
      if (paymentStatus.name().equals(status)) {
        return paymentStatus;
      }
    }
    throw new IllegalArgumentException();
  }
}
