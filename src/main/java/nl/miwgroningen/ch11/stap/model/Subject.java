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

    @ManyToMany(mappedBy = "subjects")
    private List<Course> courses;

    @OneToMany(mappedBy = "subject")
    private List<Exam> exams;

    @ManyToMany
    private List<LearningGoal> learningGoals;

    @ManyToOne
    private Teacher teacher;

    public String getDurationString() {
        if (duration < 10) {
            return String.format("%d dagen", duration);
        } else {
            return String.format("%.0f weken", duration / COURSE_DAYS_PER_WEEK);
        }
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public void removeLearningGoal(LearningGoal learningGoal) {
        learningGoals.remove(learningGoal);
    }

    public void removeTeacher() {
        this.teacher = null;
    }

    public void setDuration(int duration) {
        if (duration < 1) {
            throw new IllegalArgumentException("Duration cannot be shorter than 1 day");
        }
        this.duration = duration;
    }
}
