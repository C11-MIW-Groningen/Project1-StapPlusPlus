package nl.miwgroningen.ch11.stap.DTO;

import lombok.*;
import nl.miwgroningen.ch11.stap.model.Course;
import nl.miwgroningen.ch11.stap.model.Student;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Tristan Meinsma
 * This program helps data transfer between website and model
 */
@Getter
@Setter
public class CohortDTO {
    @Id
    @GeneratedValue
    private Long cohortDtoId;

    private String number;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ManyToOne(cascade = {CascadeType.DETACH})
    private Course course;

    @ManyToMany
    private List<Student> studentsInThisCohort;

    @OneToMany
    private List<Student> studentsNotInThisCohort;

    public void addStudent(Student student) {
        studentsInThisCohort.add(student);
    }
    public void removeStudent(Student student) {
        studentsInThisCohort.remove(student);
    }
}
