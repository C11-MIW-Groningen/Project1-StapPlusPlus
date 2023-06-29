package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Author: Thijs Harleman
 * Created at 09:15 on 13 Jun 2023
 * Purpose: a subject has 0 or more learning goals that can be tested with an exam.
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoal {
    @Id @GeneratedValue
    private Long learningGoalId;

    private String title;
    private String description;

    @ManyToMany(mappedBy = "learningGoals")
    private List<Subject> subjects;
}
