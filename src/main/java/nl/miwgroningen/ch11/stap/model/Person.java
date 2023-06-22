package nl.miwgroningen.ch11.stap.model;

import javax.persistence.*;

/**
 * @author Tristan Meinsma
 * Dit programma doet x
 */
public class Person {

    private String firstName;
    private String infixName;
    private String lastName;

//    @Override
//    public int compareTo(Student otherStudent) {
//        return this.lastName.compareTo(otherStudent.getLastName());
//    }

    public String getDisplayName() {
        if (infixName.equals("")) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, infixName, lastName);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInfixName() {
        return infixName;
    }

    public void setInfixName(String infixName) {
        this.infixName = infixName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
