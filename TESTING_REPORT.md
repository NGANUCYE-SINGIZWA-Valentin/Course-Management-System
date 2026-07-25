# Course Management System — Testing Report

## Annotation used

`@Before` creates a new `CourseService` and adds two known courses before every test. This keeps the tests independent because one test cannot change the starting data of another test.

## Test coverage

| Service method | Situation tested | Expected result |
| --- | --- | --- |
| `saveCourse` | Course is `null` | `provide course data` |
| `saveCourse` | Duplicate code with different letter case | `course already exists` |
| `saveCourse` | Credits below 1 | `invalid credits` |
| `saveCourse` | Credits above 5 | `invalid credits` |
| `saveCourse` | Valid course | `course saved successfully` |
| `searchByCourseCode` | Existing code | The saved `Course` object |
| `searchByCourseCode` | Missing or `null` code | `null` |
| `deleteCourse` | Existing code | `course deleted` |
| `deleteCourse` | Missing code | `course not found` |

## Duplicate-code question

The test uses `assertEquals("course already exists", response)` to prove the second save was rejected. It is better than `assertTrue(response.contains("exists"))` because it checks the exact required message. A different, incorrect message such as `code exists but course was saved` would still pass the `contains` test.

## Test run

Run the complete suite with:

```text
mvn test
```

All ten tests should pass.

## Manual failure check

To prove that the duplicate test can catch a bug, temporarily remove the duplicate-code check in `CourseService.saveCourse`, run `mvn test`, and observe that `saveCourse_shouldReturnAlreadyExists_whenCodeIsDuplicate` fails. Restore the check afterwards.
