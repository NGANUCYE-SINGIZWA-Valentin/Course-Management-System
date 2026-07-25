package domain;

public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private int maxStudents;

    public Course(String courseCode, String courseName, int credits, int maxStudents) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.maxStudents = maxStudents;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public int getMaxStudents() {
        return maxStudents;
    }
}
