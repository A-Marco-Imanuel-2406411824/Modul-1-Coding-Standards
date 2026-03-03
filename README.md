# <ins>MODUL 1</ins>

## Reflection 1:
There are several clean code principles used in the codebase. First, i ensured i used clear and meaningful names for my fields and methods. Then, i made sure every method just do one thing/action without side effects. I also implemented null safety by using optional class in methods that may return null. So far, i haven't encountered any mistakes while making the source code due to carefully planning the code i write, and by paying attention to the forum for previous issues.


## Reflection 2:
1. After writing the unit test, I feel more assured about the quality of the code written.
   It may not be perfect, but it is certainly producing the expected behaviour in all the case tested.
   Also, since a certain behaviour is always checked by the unit test, I can feel more confident in changing a method's implementation for improvements as the unit test can
   verify the same expected behaviour consistently regardless of how the method works; it only cares on how it acts.
   In my opinion, the amount of unit tests in a class should be as many as the methods that have some sort of logic (Conditionals, or some sort of algorithm)
   because that's where a method can deviate with its intended action.
   Also, just because a code has 100% code coverage, that doesn't mean your code has no bugs or errors, that just means the code behaves as the test intended following
   from the cases created. Some unforeseen cases could arise which won't be knowingly tested (And corrected) without hindsight.

2. Likely code-quality concerns:
      - Duplicate setup/teardown and instance variables across functional tests violate DRY; harder to maintain when endpoints or credentials change.
      - Repeated locators/URL literals are magic strings and brittle if the UI changes.
      - Mixed responsibilities: test methods may handle both navigation and assertions without abstraction, reducing readability.
      - Possible flaky timing (no waits) or missing cleanup, leading to interdependent tests.
     
      Some possible improvements:
      - Extract common WebDriver/JUnit setup into a shared base test class or reusable helper methods.
      - Apply the Page Object pattern to centralize element locators and user actions; use constants for URLs/paths.
      - Keep tests small and focused on assertions; factor navigation/form-filling into helpers.
      - Add proper waits and cleanup to ensure isolation and reduce flakiness.



# <ins>MODUL 2</ins>
1. Code quality issue(s) that are fixed during the exercise:
   - ### Field injection (Using @Autowired).
      This allows for invalid objects to be created (even briefly) as it works by injecting the fields after it is created by default,
      thus potentially causing hard to debug null pointer exceptions. It also isn't compatible with 
      final fields, making dependencies unable to be immutable. It's also harder to unit test
   
      **Solution**: Remove all @Autowired annotations on fields, and then adding 
      Lombok's @RequiredArgsConstructor annotation on the class with said fields.
      This annotation gives the same convenience and decoupling like @Autowired but does
      it by creating constructor for said fields in compile-time. 

2. I think the current implementation has met the definition of Continuous Integration 
   and Continuous Deployment. The continuous change & updates in codebase are integrated 
   by an automated GitHub actions build script. It is also verified using various tools like scorecards and 
   the SonarQube scanner i added in the CI script as well as Spotless. The codebase is also automatically deployed
   to a koyeb PaaS server using a pull based approach. Combined, the features above already 
   is an implementation of CI/CD. 

## Reflection 3
1. Principles applied:
- SRP: By making CarController and ProductController into two separate classes (Instead of it inheriting ProductController), CarController now doesn't inherit the unnecessary ProductController functionalities and instead only has its own, thus keeping the responsibilities of those two controllers separate and singular.
- DIP: By making CarController composit an object of a CarService interface instead of its implementation: CarServiceImpl, the controller now relies on the abstraction instead of the direct implementation, which reduces coupling from the dependency side of things and enhances flexibility on the dependency's part.

2. Advantages of applying SOLID:
    By applying SOLID, my project becomes more flexible (due to less coupling between features),
    more resilient, and be much easier to maintain; Applying SOLID means i actively apply
    the strengths of OOP to my project.

    Examples:
- Single Responsibility Principle (SRP): Controllers only handle HTTP orchestration, while services encapsulate business logic; easier to test ProductService in isolation with Mockito.
- Open/Closed Principle (OCP): Implement `DiscountPolicy` interface so new discount strategies are added via new classes without modifying existing checkout code.
- Liskov Substitution Principle (LSP): `InMemoryProductRepository` and `JpaProductRepository` both implement `ProductRepository`, allowing seamless swapping in tests vs production.
- Interface Segregation Principle (ISP): Split wide repository interfaces into focused ones (e.g., `ProductReadRepository`, `ProductWriteRepository`) so clients only depend on what they use.
- Dependency Inversion Principle (DIP): Controllers depend on abstractions (`ProductService`, `OrderService`) injected by Spring, improving testability and reducing coupling to concrete implementations.

3. Disadvantages of not applying SOLID:
    By not applying solid, my porject could become inflexible, fragile, stagnant, and difficult to maintain.
    OOP is a great paradigm, but it also has its vices which - if not considered carefully - could be a huge detriment
    to the project.
    
    Examples:
- Violating SRP: A `ProductController` that also validates entities and talks directly to the database becomes hard to change and unit test.
- Violating OCP: Adding a new payment method forces edits across multiple classes, increasing regression risk.
- Violating LSP: Substituting a specialized repository that throws `UnsupportedOperationException` breaks callers expecting the base contract.
- Violating ISP: A monolithic `InventoryService` interface forces consumers to implement/handle unused methods, leading to fragile mocks in tests.
- Violating DIP: Hard-coding `new EmailNotifier()` inside services blocks mocking and makes swapping providers (e.g., SMTP vs. REST) error-prone.
