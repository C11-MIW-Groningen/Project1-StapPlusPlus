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
// Intellij claims to not recognise columnNames, but the unique constraint is applied anyway.
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueExamAndStudent",
        columnNames = {"exam_exam_id", "student_student_id"})})
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentExam implements Comparable<StudentExam> {
    private static final int MINIMUM_GRADE = 1;
    private static final int MAXIMUM_GRADE = 10;

    @Id @GeneratedValue
    private Long studentExamId;

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Student student;

    @OneToMany(mappedBy = "studentExam")
    private List<StudentExamQuestion> studentExamQuestions = new ArrayList<>();

    private int pointsAttained;
    private double grade;

    public String getDisplayGrade() {
        return String.format("%.2f", grade);
    }

    public void setGrade() {
        if (!studentExamQuestions.isEmpty()) {
            double calculatedGrade = (double) pointsAttained / exam.getTotalAttainablePoints()
                    * (MAXIMUM_GRADE - MINIMUM_GRADE)
                    + MINIMUM_GRADE;

            grade = Math.max(Math.min(calculatedGrade, MAXIMUM_GRADE), MINIMUM_GRADE);
        }
    }

    public void setPointsAttained() {

        int sumPoints = 0;

        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            sumPoints += studentExamQuestion.getPointsAttained();
        }

        pointsAttained = sumPoints;
    }

    @Override
    public int compareTo(StudentExam otherStudentExam) {
        return student.compareTo(otherStudentExam.student);
    }
}
