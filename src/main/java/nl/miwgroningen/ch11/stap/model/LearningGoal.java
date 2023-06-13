package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Thijs Harleman
 * Created at 09:15 on 13 Jun 2023
 * Purpose: a subject has 1 or more learning goals that can be tested
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
}
