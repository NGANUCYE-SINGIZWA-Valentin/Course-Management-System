package service;

import domain.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private List<Course> courses = new ArrayList<>();

    public String saveCourse(Course course) {
        if (course == null) {
            return "provide course data";
        }

        if (searchByCourseCode(course.getCourseCode()) != null) {
            return "course already exists";
        }

        if (course.getCredits() < 1 || course.getCredits() > 5) {
            return "invalid credits";
        }

        courses.add(course);
        return "course saved successfully";
    }

    public Course searchByCourseCode(String code) {
        if (code == null) {
            return null;
        }

        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(code)) {
                return course;
            }
        }

        return null;
    }

    public String deleteCourse(String code) {
        Course course = searchByCourseCode(code);

        if (course == null) {
            return "course not found";
        }

        courses.remove(course);
        return "course deleted";
    }
}
