---
name: generate-spring-tests
description: Generate unit and controller tests for Spring Boot APIs using Mockito and MockMvc based on Java source code
---

# Generate Spring Boot Tests

You generate automated tests for Spring Boot applications using Mockito and MockMvc.

## When to use this skill

Use this skill when:

* The user provides or references a Java class
* The goal is to generate test classes
* The code uses Spring Boot annotations like `@Service` or `@RestController`

---

## Step 1 — Identify class type

Analyze the Java source code and detect:

* `@Service` → unit test (Mockito)
* `@RestController` → controller test (MockMvc)

---

## Step 2 — Extract dependencies

* Identify all `private final` fields
* These must be mocked in tests

---

## Step 3 — Choose test strategy

### For @Service classes

* Use `@ExtendWith(MockitoExtension.class)`
* Use `@Mock` for dependencies
* Use `@InjectMocks` for the class under test
* Do NOT start Spring context

---

### For @RestController classes

* Use `@WebMvcTest(ClassName.class)`
* Use `MockMvc`
* Use `@MockBean` for services

---

## Step 4 — Generate test cases

For each public method, generate:

1. Success case
2. Failure case (exception or invalid scenario)
3. Edge case (if applicable)

---

## Step 5 — Naming convention

Use:

should<ExpectedResult>When<Condition>

Examples:

* shouldReturnUserWhenExists
* shouldThrowExceptionWhenUserNotFound

---

## Step 6 — Heuristics

### Optional return types

If method returns Optional:

* Generate success test → `Optional.of(...)`
* Generate failure test → `Optional.empty()`

---

### Methods containing "save"

* Validate persistence behavior
* Ensure returned object matches expected

---

### Validation annotations

If input validation is detected:

* Generate invalid input test cases

---

## Step 7 — Assertions and mocking

* Use `Mockito.when(...).thenReturn(...)`
* Use `Assertions` or `AssertJ`
* Validate response fields and behavior

---

## Step 8 — Output requirements

* Generate a full test class
* Include imports
* Follow naming convention:

<ClassName>Test

---

## Step 9 — Avoid false positive tests (CRITICAL)

Do not generate tests that always pass regardless of input.

### Forbidden patterns

Avoid mocks like:

```java
when(service.method(any()))
    .thenThrow(...)
```

or

```java
when(service.method(any()))
    .thenReturn(...)
```

These patterns ignore input and create false-positive tests.

---

### Required behavior

Mocks MUST reflect input conditions.

Use argument-based logic when needed:

```java
when(service.method(any()))
    .thenAnswer(invocation -> {
        InputType input = invocation.getArgument(0);

        if (invalidCondition(input)) {
            throw new RuntimeException();
        }

        return validResult;
    });
```

Controller test rules

When testing controllers:

* Only mock the service behavior
* Do NOT simulate business rules blindly
* Ensure test input matches expected scenario

---

### Required test consistency

Each test must be logically consistent:

| Scenario | Input      | Mock Behavior  | Expected Result |
| -------- | ---------- | -------------- | --------------- |
| Success  | valid data | return success | 2xx             |
| Failure  | invalid    | throw error    | 4xx             |

---

### Validation rule

If the test input is valid, the mock MUST NOT throw an exception.

If the test input is invalid, the mock MUST simulate failure.

---

### Anti-pattern detection

Before generating the test, verify:

Does the mock depend on input?
Does the test scenario match the expected outcome?

If not, adjust the mock.

---

## Step 10 — Prefer real validation when possible

If validation logic is simple and local (e.g. enum, string match, list validation):

* Prefer NOT mocking the service blindly
* Simulate realistic behavior based on input

---

## Step 11 — Output requirements

Generate a full test class
* Include imports
* Follow naming convention:

<ClassName>Test

---

## Step 12 — Ensure mock consistency with input (CRITICAL)

Mocks must reflect realistic data behavior.

Do NOT create mocks that contradict the test input.

### Example of WRONG behavior

Input:
- roles = ["ROLE_ADMIN"]

Mock:
- repository returns empty set

This creates an unrealistic scenario and invalid test.

---

### Required behavior

Mocks must be consistent with input:

| Input            | Mock result              |
|------------------|--------------------------|
| Valid role       | return matching entity   |
| Invalid role     | return empty             |

---

### Rule

If the input represents valid data, the mock must simulate a successful lookup.

If the input represents invalid data, the mock must simulate failure.

---

### Anti-pattern

Never simulate "not found" for known valid values.

---

## Constraints

* Do NOT access real database
* Do NOT use `@SpringBootTest` unless explicitly required
* Keep tests isolated and fast
* Do NOT add redundant dependencies if spring-boot-starter-test is already present

---

## Goal

Produce clean, maintainable, and production-ready test code following Spring Boot best practices.
