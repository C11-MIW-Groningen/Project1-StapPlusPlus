package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Thijs Harleman
 * Created at 13:56 on 12 Jun 2023
 * Purpose: A natural person who follows a course
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue
    private Long studentId;

    private String firstName;
    private String infixName;
    private String lastName;

    private String privateEmail;
    private String schoolEmail;

}
