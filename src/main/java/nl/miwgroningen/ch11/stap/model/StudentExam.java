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
    private static final int MINIMUM_GRADE = 1;
    private static final int MAXIMUM_GRADE = 10;
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

    private int calculateTotalAttainablePoints() {
        int totalAttainablePoints = 0;

        for (ExamQuestion examQuestion : exam.getExamQuestions()) {
            totalAttainablePoints += examQuestion.getAttainablePoints();
        }

        return totalAttainablePoints;
    }

    public String getDisplayGrade() {
        return String.format("%.2f", grade);
    }

    public void setGrade() {
        if (studentExamQuestions.size() > 0) {
            grade = (double) pointsAttained / calculateTotalAttainablePoints()
                    * (MAXIMUM_GRADE - MINIMUM_GRADE)
                    + MINIMUM_GRADE;
        }
    }

    public void setPointsAttained() {
        int sumPoints = 0;

        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            sumPoints += studentExamQuestion.getPointsAttained();
        }

        pointsAttained = sumPoints;
    }
}
