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
    @Id @GeneratedValue
    private Long studentExamQuestionId;

    private int questionNumber;
    private int pointsAttained;
    private String feedback;

    @ManyToOne
    private StudentExam studentExam;

    @Override
    public String toString() {
        return String.format("studentExamQuestionId: %s, questionNumber: %s, pointsAttained: %s, feedback: %s, studentExam: %s", this.studentExamQuestionId, this.questionNumber, this.pointsAttained, this.feedback, this.studentExam);
    }
}
