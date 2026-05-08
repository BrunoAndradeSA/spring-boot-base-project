---
name: generate-spring-tests
description: Generate unit and controller tests for Spring Boot APIs using Mockito and MockMvc based on Java source code
---

# Generate Spring Boot Tests

Generate automated tests for Spring Boot applications using Mockito and MockMvc.

---

# Step 1 — Identify class type and test strategy

| Annotation | Strategy | Key Setup |
|---|---|---|
| `@Service` | Unit test (Mockito) | `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks` — no Spring context |
| `@RestController` | Controller test (MockMvc) | `@WebMvcTest(ClassName.class)`, `MockMvc`, `@MockBean` for services |
| `@RestControllerAdvice` | MockMvc only | `@WebMvcTest` + minimal test controller that throws the exception — no direct method calls on the handler |

Identify all `private final` fields in the class under test — each becomes a `@Mock` or `@MockBean`.

---

# Step 2 — Define test cases per method

For each public method, generate only the cases that have distinct observable behavior:

1. **Success case** — valid input, mock returns expected result
2. **Failure case** — invalid or missing input, mock throws exception or returns empty
3. **Edge case** — only when the method has explicit branching for it (e.g., `Optional.empty()`, boundary values)

> Do not generate a test case just to increase coverage. A case is only justified if its outcome differs from existing cases.

Treat `Optional.empty()` as a distinct observable behavior when it changes the execution flow.

---

# Step 3 — Naming convention

Use the pattern:

```java
should<ExpectedResult>When<Condition>
````

Examples:

```java
shouldReturnUserWhenExists
shouldThrowNotFoundWhenUserMissing
shouldReturnBadRequestWhenPayloadInvalid
```

---

# Step 4 — Mock isolation (CRITICAL)

Each test must have its own focused mock setup.

Never write a single mock that conditionally handles multiple scenarios via `thenAnswer` with `if/else` branching.

## Forbidden

```java
when(service.find(any())).thenAnswer(invocation -> {
    String id = invocation.getArgument(0);

    if (id == null) {
        throw new NotFoundException();
    }

    return result;
});
```

## Required

```java
// success test
when(service.find(eq(VALID_ID)))
    .thenReturn(expectedUser);

// failure test
when(service.find(eq(UNKNOWN_ID)))
    .thenThrow(new NotFoundException());
```

Use specific matchers (`eq(...)`) over `any()` whenever the input value is known.

Reserve `any()` only when the argument value is genuinely irrelevant to the behavior being tested.

Do not use `lenient()`.

Remove unused stubs instead of suppressing warnings.

---

# Step 5 — Assertions: one behavior per test

Each test must assert exactly what it is named after — no more.

## Unit tests

Use AssertJ:

```java
assertThat(...)
assertThatThrownBy(...)
```

## Controller tests

Use:

```java
status()
jsonPath()
content()
```

Controller tests must always validate:

* HTTP status
* response content type when applicable
* only the relevant response fields for the scenario

## Constants

Use constants from the codebase instead of magic values.

### Good

```java
assertThat(response.getStatus())
    .isEqualTo(AppConstants.SC_GENERIC_ERROR);
```

### Bad

```java
assertThat(response.getStatus())
    .isEqualTo(-99);
```

## Structural contract tests

Create only one structural contract test per response type.

This test validates that all expected response fields exist.

All other tests must assert only the field(s) relevant to the behavior being tested.

> Asserting unrelated fields in a behavioral test is noise and creates fragile tests.

---

# Step 6 — Interaction verification

Use `verify(...)` only when the interaction itself is part of the behavior being tested.

## Good examples

* ensuring a fallback method is called
* ensuring an event is published
* ensuring a retry does not happen

## Avoid

```java
verify(repository).save(any());
verify(service).find(any());
```

unless the interaction itself is the business rule.

---

# Step 7 — Validation tests

Prefer real Spring validation over manually constructing framework exceptions.

## Good

```java
mockMvc.perform(post("/users")
        .content("{}")
        .contentType(APPLICATION_JSON))
    .andExpect(status().isBadRequest());
```

## Bad

```java
MethodArgumentNotValidException ex =
    mock(MethodArgumentNotValidException.class);

handler.handleValidationException(ex);
```

Validation tests must exercise the real HTTP validation flow.

---

# Step 8 — Object creation and over-mocking

Do not mock:

* DTOs
* entities
* collections
* value objects

Instantiate real objects whenever possible.

Mock only:

* external collaborators
* gateways
* repositories
* expensive dependencies
* integrations

When object setup becomes repetitive:

* extract helper methods
* prefer semantic builders

Examples:

```java
buildUser()
buildValidRequest()
buildResponse()
```

Avoid large inline object construction inside tests.

---

# Step 9 — Private methods

Never test private methods directly.

Private behavior must be exercised through public methods.

Do not use reflection to invoke private methods.

---

# Step 10 — Deterministic tests

Never use:

```java
LocalDate.now()
LocalDateTime.now()
new Date()
```

directly in tests.

Prefer fixed values to keep tests deterministic and non-flaky.

---

# Step 11 — Exception assertions

Avoid asserting full exception messages unless the exact message is part of the business contract.

Prefer asserting:

* exception type
* error code
* response field
* status code

over complete message text.

---

# Step 12 — Nested tests

Use `@Nested` only when a method has multiple clearly separated behavioral groups.

Avoid excessive nesting.

---

# Step 13 — Avoid low-value tests

Do not generate tests for:

* trivial getters/setters
* Lombok-generated behavior
* simple DTO mappings with no logic
* framework behavior already covered by Spring
* repository CRUD behavior already guaranteed by JPA

Focus on business behavior and observable outcomes.

---

# Step 14 — Output requirements

* Generate a full test class with all imports
* Class name: `<ClassName>Test`
* Do not use `@SpringBootTest`
* Do not add dependencies already provided by `spring-boot-starter-test`
* Keep each test method under ~15 lines
* If a test becomes too long, extract setup helpers or split responsibilities