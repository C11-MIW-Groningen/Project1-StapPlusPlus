package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Author: Thijs Harleman
 * Created at 13:58 on 12 Jun 2023
 * Purpose: a course subject that is graded
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue
    private Long subjectId;

    private String title;
    private int duration;

    @ManyToMany
    private List<LearningGoal> learningGoals;
}
