package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Tristan Meinsma
 * A cohort is a class of students in a specific moment
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Cohort {
    @Id
    @GeneratedValue
    private Long cohortId;

    private int number;
    private LocalDate startDate;

    @ManyToOne
    private Course course;

    @ManyToMany
    private List<Student> students;
}