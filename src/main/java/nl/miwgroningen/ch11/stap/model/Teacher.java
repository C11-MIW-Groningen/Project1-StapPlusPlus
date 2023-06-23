package nl.miwgroningen.ch11.stap.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * Author: Thijs Harleman
 * Created at 13:57 on 12 Jun 2023
 * Purpose: a natural person who teaches a course
 */

@Entity
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Teacher extends Person {
    @Id
    @GeneratedValue
    private Long teacherId;

    @OneToMany(mappedBy = "teacher")
    private List<Subject> subjects;
}
