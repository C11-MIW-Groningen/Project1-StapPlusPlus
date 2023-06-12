package nl.miwgroningen.ch11.stap.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Thijs Harleman
 * Created at 13:58 on 12 Jun 2023
 * Purpose: a course subject that is graded
 */

@Entity
@Getter @Setter
public class Subject {
    @Id
    @GeneratedValue
    private Long subjectId;

    private String title;
    private int duration;
}
