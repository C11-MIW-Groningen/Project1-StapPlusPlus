package nl.miwgroningen.ch11.stap.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tristan Meinsma
 * Checks if display name works correctly
 */
class PersonTest {

    private Student student;
    private Teacher teacher;

    @BeforeEach
    void setup() {
        student = Student.builder().firstName("John").infixName("")
                .lastName("Doe").build();
        teacher = Teacher.builder().firstName("Sarah").infixName("")
                .lastName("Doe").build();
    }

    @Test
    @DisplayName("should add a space when no infix present")
    void shouldAddASpaceWhenNoInfixPresent() {
        String expectedNameStudent = "John Doe";
        String expectedNameTeacher = "Sarah Doe";

        assertAll(() -> assertEquals(expectedNameStudent, student.getDisplayName()),
                () -> assertEquals(expectedNameTeacher, teacher.getDisplayName()));
    }


    @Test
    @DisplayName("should add a space around infix when present")
    void shouldAddASpaceAroundInfixWhenPresent() {
        student.setInfixName("von");
        teacher.setInfixName("von");

        String expectedNameStudent = "John von Doe";
        String expectedNameTeacher = "Sarah von Doe";

        assertAll(() -> assertEquals(expectedNameStudent, student.getDisplayName()),
                () -> assertEquals(expectedNameTeacher, teacher.getDisplayName()));
    }
}