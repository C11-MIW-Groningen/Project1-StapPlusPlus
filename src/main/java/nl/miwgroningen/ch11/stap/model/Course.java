package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.*;
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

    @Column(unique = true)
    private String name;

    @ManyToMany
    private List<Subject> subjects;

    private String imageUrl;
    private String description;
}
