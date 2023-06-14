package nl.miwgroningen.ch11.stap.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

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
public class Student implements Comparable <Student>{
    @Id
    @GeneratedValue
    private Long studentId;

    private String firstName;
    private String infixName;
    private String lastName;

    private String privateEmail;
    private String schoolEmail;

    @ManyToMany
    private List<Course> courses;

    @Override
    public int compareTo(Student otherStudent) {
        return this.lastName.compareTo(otherStudent.getLastName());
    }

    public String getDisplayName() {
        if (infixName.equals("")) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, infixName, lastName);
        }
    }
}
