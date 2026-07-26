# TESTING_REPORT.md

# Software Testing Practical Assignment

**Student Name:** Nganucye Singizwa Valentin
**Student ID:** 27201
**Course:** Software Testing
**Project:** Course Management System
**Semester:** Semester Two, 2025–2026
**Date:** 25 July 2026
**GitHub:** https://github.com/NGANUCYE-SINGIZWA-Valentin/Course-Management-System

---

# 1. Introduction

This report describes the testing activities carried out for the Course Management System project using JUnit 4 and Maven. The objective was to implement the required business rules and verify that they work correctly using unit tests, lifecycle annotations, and JUnit assertions.

The project contains two test classes:
- `LifecycleDemoTest` — the warm-up class that proves the JUnit 4 lifecycle order visually.
- `CourseServiceTest` — the main test class covering all business rules of `CourseService`.

---

# 2. JUnit Lifecycle Annotations Used

## @BeforeClass

Used in `CourseServiceTest` to print a suite-start message once before all tests run. This simulates opening a shared resource (e.g. a database connection) that is expensive to create and safe to reuse across all tests. Must be `static` because JUnit 4 calls it before creating any test instance.

## @Before

Used in `CourseServiceTest` to create a fresh `CourseService` and pre-load it with two known courses (`CS301` and `CS302`) before every single test. This ensures each test starts from the same clean state and cannot be affected by what a previous test did.

## @After

Used in `CourseServiceTest` to null out the `courseService` reference after every test, even if the test fails. This releases the object and proves teardown always runs regardless of test outcome.

## @AfterClass

Used in `CourseServiceTest` to print a suite-end message once after all tests have finished. This simulates closing a shared resource. Must be `static` for the same reason as `@BeforeClass`.

## @Test

Marks a method as a test case to be discovered and executed by the JUnit runner.

---

# 3. Warm-Up — Lifecycle Demo

### Class: `LifecycleDemoTest`

This class was created to prove the exact order JUnit 4 runs lifecycle annotations.

**Expected order (written before running):**
```
>> BeforeClass
>> Before
>> Test 1
>> After
>> Before
>> Test 2
>> After
>> AfterClass
```

**Actual console output after running `mvn test`:**
```
>> BeforeClass
>> Before
>> Test 1
>> After
>> Before
>> Test 2
>> After
>> AfterClass
```

The prediction was correct.

---

# 4. Written Answers

### Why must `@BeforeClass` and `@AfterClass` be static in JUnit 4?

JUnit 4 creates a brand-new instance of the test class for every `@Test` method. `@BeforeClass` must run exactly once before any instance is created, and `@AfterClass` must run once after all instances are gone. Because no instance exists at those moments, the method cannot belong to an instance — it must belong to the class itself, which means it must be `static`.

### When would you use `@Before` instead of `@BeforeClass`?

Use `@Before` when each test needs its own fresh, independent copy of a resource — for example, a new `List` or a new service object. If two tests shared the same list and one test added an item to it, the second test would see unexpected data and could give a false result.

Use `@BeforeClass` when the resource is expensive to create and safe to share across all tests — for example, opening a real database connection or starting an embedded HTTP server. Creating it once and reusing it saves significant time.

**Concrete example from this project:** `@Before` creates a new `CourseService` with two pre-loaded courses for every test. If `@BeforeClass` were used instead, a test that deletes `CS301` would break every test that runs after it.

### Does `@After` still run if the test fails?

Yes. `@After` is guaranteed to run after every test regardless of whether the test passed, failed, or threw an exception. This is by design — it ensures cleanup always happens.

**Proof:** Add `assertTrue(false)` inside `test1()` in `LifecycleDemoTest`, run `mvn test`, and the console still prints `>> After` after `>> Test 1`, confirming `@After` ran despite the failure.

### Duplicate-check trick question

The test saves `"CS301"` (already pre-loaded in `@Before`) and then tries to save `"cs301"`.

`assertEquals("course already exists", response)` is better than `assertTrue(response.contains("exists"))` because it checks the **exact** required message character by character. A wrong message such as `"code exists but course was saved anyway"` would still pass the `contains` check, hiding a real bug. `assertEquals` fails unless the string matches exactly.

---

# 5. Test Cases Implemented

### `CourseServiceTest` — 10 tests

| Test Method | Rule Tested | Assertion Used | Expected Result |
|---|---|---|---|
| `saveCourse_shouldReturnProvideData_whenCourseIsNull` | null course rejected | `assertEquals` | `"provide course data"` |
| `saveCourse_shouldReturnAlreadyExists_whenCodeIsDuplicate` | duplicate code (case-insensitive) rejected | `assertEquals` | `"course already exists"` |
| `saveCourse_shouldReturnInvalidCredits_whenCreditsAreBelowOne` | credits = 0, below boundary | `assertEquals` | `"invalid credits"` |
| `saveCourse_shouldReturnInvalidCredits_whenCreditsAreAboveFive` | credits = 6, above boundary | `assertEquals` | `"invalid credits"` |
| `saveCourse_shouldSaveCourse_whenCourseIsValid` | valid course saves | `assertEquals` | `"course saved successfully"` |
| `searchByCourseCode_shouldReturnCourse_whenCodeExists` | finds by code (case-insensitive) | `assertNotNull` + `assertEquals` + `assertSame` | the saved `Course` object |
| `searchByCourseCode_shouldReturnNull_whenCodeDoesNotExist` | missing code returns null | `assertNull` | `null` |
| `searchByCourseCode_shouldReturnNull_whenCodeIsNull` | null code returns null | `assertNull` | `null` |
| `deleteCourse_shouldReturnDeleted_whenCodeExists` | deletes and confirms gone | `assertEquals` + `assertNull` | `"course deleted"` |
| `deleteCourse_shouldReturnNotFound_whenCodeDoesNotExist` | missing code | `assertEquals` | `"course not found"` |

---

# 6. Partition and Boundary Table — `saveCourse`

| Input | Partition | Expected Result |
|---|---|---|
| `null` | Null input | `"provide course data"` |
| `"cs301"` (duplicate, different case) | Duplicate | `"course already exists"` |
| credits = 0 | Below lower boundary (min − 1) | `"invalid credits"` |
| credits = 1 | At lower boundary | `"course saved successfully"` |
| credits = 3 | Valid middle value | `"course saved successfully"` |
| credits = 5 | At upper boundary | `"course saved successfully"` |
| credits = 6 | Above upper boundary (max + 1) | `"invalid credits"` |
| All fields valid, unique code | Happy path | `"course saved successfully"` |

---

# 7. Assertions Used

| Assertion | Where used | Why |
|---|---|---|
| `assertEquals` | All `saveCourse` and `deleteCourse` tests | Checks the exact required message string |
| `assertNull` | `searchByCourseCode` (missing/null code), `deleteCourse` (confirms deletion) | Verifies nothing is returned |
| `assertNotNull` | `searchByCourseCode` (existing code) | Verifies a result was returned before inspecting it |
| `assertSame` | `searchByCourseCode` (existing code) | Proves the service returns the exact same object reference, not a copy |

---

# 8. Testing Results

All tests were executed using:

```
mvn test
```

**Total: 12 tests — 10 in `CourseServiceTest`, 2 in `LifecycleDemoTest`**

### Passing Screenshot (all green — BUILD SUCCESS)

![All tests passing](Testing_Images/test1_mvn_test_build_successfull_removing_comments.png)

### Failing Screenshot (intentional bug to prove tests detect errors)

The duplicate-check block in `CourseService.saveCourse` was commented out (lines 13–15). Running `mvn test` immediately caused `saveCourse_shouldReturnAlreadyExists_whenCodeIsDuplicate` to fail with:

```
expected:<course [already exists]> but was:<course [saved successfully]>
```

This proves the test is not a false positive — it genuinely detects the missing business rule.

![Failing test](Testing_Images/test1_failure_commenting_on_line_13_15.png)

The duplicate-check was then restored and all 12 tests passed again.

---

# 9. Conclusion

The project successfully implemented all required business rules in `CourseService` and verified them with 10 targeted unit tests. The warm-up `LifecycleDemoTest` confirmed the exact execution order of JUnit 4 lifecycle annotations. All four assertion types required by the assignment (`assertEquals`, `assertNull`, `assertNotNull`, `assertSame`) were used in the correct context. Boundary values at credits = 0, 1, 5, and 6 were explicitly tested. The intentional failure screenshot proves the test suite can detect real bugs, not just pass blindly.
