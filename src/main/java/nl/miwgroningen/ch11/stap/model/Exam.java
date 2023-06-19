package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
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
public class Exam {
    @Id @GeneratedValue
    private Long examId;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Cohort cohort;

    @OneToMany(mappedBy = "exam")
    private List<ExamQuestion> examQuestions;

    private boolean resit;
}
