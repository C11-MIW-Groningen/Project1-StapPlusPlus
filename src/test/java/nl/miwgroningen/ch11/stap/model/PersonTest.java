package nl.miwgroningen.ch11.stap.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tristan Meinsma
 * Checks if display name works correctly
 */
class PersonTest {

    @Test
    @DisplayName("should add a space when no infix present")
    void shouldAddASpaceWhenNoInfixPresent() {
        Student student = Student.builder().firstName("John").infixName("")
                .lastName("Doe").build();
        Teacher teacher = Teacher.builder().firstName("Sarah").infixName("")
                .lastName("Doe").build();

        String expectedNameStudent = "John Doe";
        String expectedNameTeacher = "Sarah Doe";

        assertAll(() -> assertEquals(expectedNameStudent, student.getDisplayName()),
                () -> assertEquals(expectedNameTeacher, teacher.getDisplayName()));
    }


    @Test
    @DisplayName("should add a space around infix when present")
    void shouldAddASpaceAroundInfixWhenPresent() {
        Student newStudent = Student.builder().firstName("John").infixName("von")
                .lastName("Doe").build();
        Teacher newTeacher = Teacher.builder().firstName("Sarah").infixName("von")
                .lastName("Doe").build();

        String expectedNameStudent = "John von Doe";
        String expectedNameTeacher = "Sarah von Doe";

        assertAll(() -> assertEquals(expectedNameStudent, newStudent.getDisplayName()),
                () -> assertEquals(expectedNameTeacher, newTeacher.getDisplayName()));
    }
}