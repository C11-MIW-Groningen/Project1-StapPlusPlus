package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author Tristan Meinsma
 * This is a course that contains subjects, a course is followed by a student
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long courseId;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "course")
    private Set<Cohort> cohorts;

    @ManyToMany
    private List<Subject> subjects;

    @Column(length = 2048)
    private String imageUrl;

    @OneToOne
    private Image image;

    private String description;

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }
}
