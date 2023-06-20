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
    private static final float COURSE_DAYS_PER_WEEK = 5f;

    @Id
    @GeneratedValue
    private Long subjectId;

    private String title;
    private int duration;
    private String description;

    @ManyToOne
    private Teacher teacher;

    @ManyToMany
    private List<LearningGoal> learningGoals;

    public String getDurationString() {
        if (duration < 10) {
            return String.format("%d dagen", duration);
        } else {
            return String.format("%.0f weken", duration / COURSE_DAYS_PER_WEEK);
        }
    }

    public void removeLearningGoal(LearningGoal learningGoal) {
        learningGoals.remove(learningGoal);
    }
}
