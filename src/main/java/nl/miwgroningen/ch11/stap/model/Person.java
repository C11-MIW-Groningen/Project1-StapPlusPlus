package nl.miwgroningen.ch11.stap.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Tristan Meinsma
 * This is a generic person
 */

@Getter @Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Person implements Comparable<Person> {

    @Column(nullable = false)
    private String firstName;
    private String infixName;
    @Column(nullable = false)
    private String lastName;

    @Override
    public int compareTo(Person otherPerson) {
        return this.lastName.compareTo(otherPerson.getLastName());
    }

    public String getDisplayName() {
        if (infixName.equals("")) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, infixName, lastName);
        }
    }
}
