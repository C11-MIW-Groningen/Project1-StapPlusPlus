package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Thijs Harleman
 * Created at 09:38 on 19 Jun 2023
 * Purpose: a handed in student response for an exam
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentExam {
    @Id @GeneratedValue
    private Long studentExamId;

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Student student;

    @OneToMany(mappedBy = "studentExam", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<StudentExamQuestion> studentExamQuestions = new ArrayList<>();

    private int pointsAttained;
    private double grade;
}
