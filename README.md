Reflection 1:
There are several clean code principles used in the codebase. First, i ensured i used clear and meaningful names for my fields and methods. Then, i made sure every method just do one thing/action without side effects. I also implemented null safety by using optional class in methods that may return null. So far, i haven't encountered any mistakes while making the source code due to carefully planning the code i write, and by paying attention to the forum for previous issues.


Reflection 2:
1. After writing the unit test, i feel more assured about the quality of the code written.
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
   
NOTE: New file structure in main and ci-cd branches for the second module
test