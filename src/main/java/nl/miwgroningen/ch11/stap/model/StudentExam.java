package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
    private StudentExamQuestion studentExamQuestion;

    private int pointsAttained;
    private double grade;
}
