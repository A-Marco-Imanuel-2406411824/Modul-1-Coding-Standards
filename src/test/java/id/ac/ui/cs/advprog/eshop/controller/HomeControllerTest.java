package id.ac.ui.cs.advprog.eshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

  private HomeController homeController;

  @BeforeEach
  void setUp() {
    homeController = new HomeController();
  }

  @Test
  void homePageReturnsHomepageView() {
    String viewName = homeController.homePage();

    assertEquals("Homepage", viewName);
  }
}

