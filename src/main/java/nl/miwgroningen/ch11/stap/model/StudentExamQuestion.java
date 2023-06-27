package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Author: Thijs Harleman
 * Created at 09:41 on 19 Jun 2023
 * Purpose: a handed in student response for a question that is part of an exam.
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamQuestion {
    private static final int INVALID_QUESTION_POINTS = -1;

    @Id @GeneratedValue
    private Long studentExamQuestionId;

    private int questionNumber;
    private int pointsAttained;
    private String feedback;

    @ManyToOne
    private StudentExam studentExam;

    public int getAttainablePoints() {
        for (ExamQuestion examQuestion : studentExam.getExam().getExamQuestions()) {
            if (examQuestion.getQuestionNumber() == questionNumber) {
                return examQuestion.getAttainablePoints();
            }
        }

        return INVALID_QUESTION_POINTS;
    }
}
