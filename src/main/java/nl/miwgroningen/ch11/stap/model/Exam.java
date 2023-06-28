package nl.miwgroningen.ch11.stap.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Author: Thijs Harleman
 * Created at 09:21 on 19 Jun 2023
 * Purpose: A gradable test a students makes to complete a course subject.
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exam implements Comparable<Exam> {
    @Id @GeneratedValue
    private Long examId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;
    private boolean resit;

    @ManyToOne
    private Cohort cohort;

    @OneToMany(mappedBy = "exam")
    private List<ExamQuestion> examQuestions;

    @OneToMany(mappedBy = "exam")
    private List<StudentExam> studentExams;

    @ManyToOne
    private Subject subject;

    public String getDisplayExamTitle() {
        return String.format("Tentamen %s", (subject == null) ? "" : subject.getTitle());

    }

    public String getDisplayExamSubtitle() {
        return String.format("Cohort %s%s",
                (cohort == null) ? "geen" : cohort.getName(),
                resit ? " (herkansing)" : "");
    }

    public int getTotalAttainablePoints() {
        int totalAttainablePoints = 0;

        for (ExamQuestion examQuestion : examQuestions) {
            totalAttainablePoints += examQuestion.getAttainablePoints();
        }

        if (totalAttainablePoints > 0) {
            return totalAttainablePoints;
        } else {
            System.err.println("Total points for exam cannot be 0 or less");
            return -1;
        }
    }

    public void removeCohort() {
        this.cohort = null;
    }

    @Override
    public int compareTo(Exam otherExam) {
        return examDate.compareTo(otherExam.examDate);
    }
}
