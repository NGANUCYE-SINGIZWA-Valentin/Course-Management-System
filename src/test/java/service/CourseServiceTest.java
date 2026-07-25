package service;

import domain.Course;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CourseServiceTest {
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = new CourseService();
        courseService.saveCourse(new Course("CS301", "Software Testing", 3, 40));
        courseService.saveCourse(new Course("CS302", "Database Systems", 4, 35));
    }

    @Test
    public void saveCourse_shouldReturnProvideData_whenCourseIsNull() {
        String response = courseService.saveCourse(null);

        assertEquals("provide course data", response);
    }

    @Test
    public void saveCourse_shouldReturnAlreadyExists_whenCodeIsDuplicate() {
        String response = courseService.saveCourse(
                new Course("cs301", "Another Testing Course", 3, 30));

        // assertEquals checks the exact rule message, not only one word in it.
        assertEquals("course already exists", response);
    }

    @Test
    public void saveCourse_shouldReturnInvalidCredits_whenCreditsAreBelowOne() {
        String response = courseService.saveCourse(
                new Course("CS303", "Networks", 0, 45));

        assertEquals("invalid credits", response);
    }

    @Test
    public void saveCourse_shouldReturnInvalidCredits_whenCreditsAreAboveFive() {
        String response = courseService.saveCourse(
                new Course("CS303", "Networks", 6, 45));

        assertEquals("invalid credits", response);
    }

    @Test
    public void saveCourse_shouldSaveCourse_whenCourseIsValid() {
        String response = courseService.saveCourse(
                new Course("CS303", "Computer Networks", 3, 45));

        assertEquals("course saved successfully", response);
    }

    @Test
    public void searchByCourseCode_shouldReturnCourse_whenCodeExists() {
        Course savedCourse = new Course("CS303", "Computer Networks", 3, 45);
        courseService.saveCourse(savedCourse);

        Course foundCourse = courseService.searchByCourseCode("cs303");

        assertNotNull(foundCourse);
        assertEquals("Computer Networks", foundCourse.getCourseName());
        assertSame(savedCourse, foundCourse);
    }

    @Test
    public void searchByCourseCode_shouldReturnNull_whenCodeDoesNotExist() {
        Course foundCourse = courseService.searchByCourseCode("CS999");

        assertNull(foundCourse);
    }

    @Test
    public void searchByCourseCode_shouldReturnNull_whenCodeIsNull() {
        Course foundCourse = courseService.searchByCourseCode(null);

        assertNull(foundCourse);
    }

    @Test
    public void deleteCourse_shouldReturnDeleted_whenCodeExists() {
        String response = courseService.deleteCourse("cs301");

        assertEquals("course deleted", response);
        assertNull(courseService.searchByCourseCode("CS301"));
    }

    @Test
    public void deleteCourse_shouldReturnNotFound_whenCodeDoesNotExist() {
        String response = courseService.deleteCourse("CS999");

        assertEquals("course not found", response);
    }
}
