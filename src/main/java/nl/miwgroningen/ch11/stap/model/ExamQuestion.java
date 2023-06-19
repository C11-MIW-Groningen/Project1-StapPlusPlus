package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Author: Thijs Harleman
 * Created at 09:26 on 19 Jun 2023
 * Purpose: a question that is part of an exam.
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestion {
    @Id @GeneratedValue
    private Long examQuestionId;

    @ManyToOne
    private Exam exam;

    private int questionNumber;
    private int attainablePoints;
    private String questionText;
}
