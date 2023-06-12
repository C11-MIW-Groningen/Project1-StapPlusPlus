package nl.miwgroningen.ch11.stap.model;

import lombok.Getter;
import lombok.Setter;

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
public class Teacher {
    @Id
    @GeneratedValue
    private Long teacherId;
}
