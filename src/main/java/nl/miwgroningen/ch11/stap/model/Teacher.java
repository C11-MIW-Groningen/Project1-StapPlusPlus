package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Author: Thijs Harleman
 * Created at 13:57 on 12 Jun 2023
 * Purpose: a natural person who teaches a course
 */

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher implements Comparable <Teacher> {
    @Id
    @GeneratedValue
    private Long teacherId;

    private String firstName;
    private String infixName;
    private String lastName;

    @Override
    public int compareTo(Teacher otherTeacher) {
        return this.lastName.compareTo(otherTeacher.getLastName());
    }

    public String getDisplayName() {
        if (infixName.equals("")) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, infixName, lastName);
        }
    }
}
