package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import java.util.List;
import lombok.Getter;

@Getter
public class Order {
  String id;
  List<Product> products;
  Long orderTime;
  String author;
  OrderStatus status;

  public Order(String id, List<Product> products, Long orderTime, String author) {
    this.id = id;
    this.orderTime = orderTime;
    this.author = author;
    this.status = OrderStatus.WAITING_PAYMENT;

    if (products.isEmpty()) {
      throw new IllegalArgumentException();
    } else {
      this.products = products;
    }
  }

  public Order(String id, List<Product> products, Long orderTime, String author, String status) {
    this(id, products, orderTime, author);
    this.setStatus(status);
  }

  public String getStatus() {
    return status.getValue();
  }

  public void setStatus(String status) {
    this.status = OrderStatus.fromString(status);
  }
}
