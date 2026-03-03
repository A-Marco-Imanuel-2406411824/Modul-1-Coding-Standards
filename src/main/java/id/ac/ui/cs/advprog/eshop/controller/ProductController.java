package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService service;

  @GetMapping("/create")
  public String createProductPage(Model model) {
    Product product = new Product();
    model.addAttribute("product", product);
    return "CreateProduct";
  }

  @PostMapping("/create")
  public String createProductPost(@ModelAttribute Product product, Model model) {
    service.create(product);
    return "redirect:list";
  }

  @GetMapping("/list")
  public String productListPage(Model model) {
    List<Product> allProducts = service.findAll();
    model.addAttribute("products", allProducts);
    return "ProductList";
  }

  @GetMapping("/edit/{id}")
  public String editProductPage(@PathVariable("id") String id, Model model) {
    Optional<Product> product = service.findById(id);
    if (!product.isPresent()) {
      return "redirect:/product/list";
    }
    model.addAttribute("product", product.get());
    return "EditProduct";
  }

  @PostMapping("/edit")
  public String editProductPost(@ModelAttribute Product product) {
    service.update(product);
    return "redirect:/product/list";
  }

  @PostMapping("/delete/{id}")
  public String deleteProduct(@PathVariable("id") String id) {
    service.deleteById(id);
    return "redirect:/product/list";
  }
}

// NOTE: constructor is made manually due to lombok requiredArgsConstructor on
// parent class
@Controller
@RequestMapping("/car")
class CarController extends ProductController {
  private final CarServiceImpl carService;

  public CarController(ProductService productService, CarServiceImpl carService) {
    super(productService);
    this.carService = carService;
  }

  @GetMapping("/createCar")
  public String createCarPage(Model model) {
    Car car = new Car();
    model.addAttribute("car", car);
    return "CreateCar";
  }

  @PostMapping("/createCar")
  public String createCarPost(@ModelAttribute Car car, Model model) {
    carService.create(car);
    return "redirect:listCar";
  }

  @GetMapping("/listCar")
  public String carListPage(Model model) {
    List<Car> allCars = carService.findAll();
    model.addAttribute("cars", allCars);
    return "CarList";
  }

  @GetMapping("/editCar/{carId}")
  public String editCarPage(@PathVariable String carId, Model model) {
    Car car = carService.findById(carId);
    model.addAttribute("car", car);
    return "EditCar";
  }

  @PostMapping("/editCar")
  public String editCarPost(@ModelAttribute Car car, Model model) {
    System.out.println(car.getCarId());
    carService.update(car.getCarId(), car);
    return "redirect:listCar";
  }

  @PostMapping("/deleteCar")
  public String deleteCar(@RequestParam("carId") String carId) {
    carService.deleteCarById(carId);
    return "redirect:listCar";
  }
}
