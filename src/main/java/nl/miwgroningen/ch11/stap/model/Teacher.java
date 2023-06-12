package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Thijs Harleman
 * Created at 13:57 on 12 Jun 2023
 * Purpose: a natural person who teaches a course
 */

@Entity
@Getter @Setter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    private Long teacherId;

    private String firstName;
    private String infixName;
    private String lastName;
}
