package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Tristan Meinsma
 * A cohort is a class of students in a specific moment
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Intellij claims to not recognise columnNames, but the unique constraint is applied anyway.
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCohortAndCourse", columnNames = {"number", "course_course_id"})})

public class Cohort {
    @Id
    @GeneratedValue
    private Long cohortId;

    private String number;

    @OneToMany(mappedBy = "cohort")
    private List<Exam> exams;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ManyToOne(cascade = {CascadeType.DETACH})
    private Course course;

    @ManyToMany
    private List<Student> students;

    public void removeCourse() {
        course = null;
    }

    public void removeStudent(Student student) {
        if (!students.contains(student)){
            throw new IllegalArgumentException("Student not present in this cohort");
        }
        students.remove(student);
    }

    public void addStudent(Student student) {
        if (students.contains(student)) {
            throw new IllegalArgumentException("Student already present in this cohort");
        }
        students.add(student);
    }
}
