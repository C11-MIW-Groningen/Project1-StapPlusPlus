package nl.miwgroningen.ch11.stap.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Author: Thijs Harleman
 * Created at 13:56 on 12 Jun 2023
 * Purpose: A natural person who follows a course
 */

@Entity
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Student extends Person {
    @Id
    @GeneratedValue
    private Long studentId;

    private String privateEmail;
    private String schoolEmail;

    @ManyToMany(mappedBy = "students")
    private List<Cohort> cohorts;

    @OneToMany(mappedBy = "student")
    private Set<StudentExam> studentExams;

}
