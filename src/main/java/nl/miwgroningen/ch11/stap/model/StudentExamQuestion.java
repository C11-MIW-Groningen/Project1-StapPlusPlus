package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private Long studentExamQuestion;

    private int questionNumber;
    private int pointsAttained;
    private String feedback;
}
