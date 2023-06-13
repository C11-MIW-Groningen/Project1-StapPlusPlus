package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

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
    private String name;

    @OneToMany
    private List<Subject> subjects;

    private String imageUrl;
    private String description;
}
