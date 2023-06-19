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

    @ManyToOne
    private Cohort cohort;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    @ManyToOne
    private Subject subject;

    @OneToMany(mappedBy = "exam")
    private List<ExamQuestion> examQuestions;

    private boolean resit;

    @Override
    public int compareTo(Exam otherExam) {
        return examDate.compareTo(otherExam.examDate);
    }
}
