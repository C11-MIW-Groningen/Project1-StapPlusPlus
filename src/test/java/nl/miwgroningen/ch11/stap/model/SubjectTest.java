package nl.miwgroningen.ch11.stap.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tristan Meinsma
 * Tests if duration is displayed correctly
 */
class SubjectTest {

    private Subject subject;

    private void createSubjectWithDuration(int duration) {
        subject = new Subject();
        subject.setDuration(duration);
    }

    @ParameterizedTest
    @DisplayName("Should throw illegal argument exception")
    @ValueSource(ints = {0, -1, -100})
    void shouldThrowIllegalArgumentException(int invalidNumberOfDays) {
        assertThrows(IllegalArgumentException.class,
                () -> createSubjectWithDuration(invalidNumberOfDays));
    }

    @Test
    @DisplayName("Should return 1 day")
    void shouldReturn1Day() {
        createSubjectWithDuration(1);
        String expectedString = "1 dag";

        assertEquals(expectedString, subject.getDurationString());
    }

    @ParameterizedTest
    @DisplayName("Should show the correct amount of days")
    @ValueSource(ints = {2, 5, 9})
    void shouldShowTheCorrectAmountOfDays(int expectedNumberOfDays) {
        createSubjectWithDuration(expectedNumberOfDays);
        String expectedString = expectedNumberOfDays + " dagen";

        assertEquals(expectedString, subject.getDurationString());
    }

    @ParameterizedTest
    @DisplayName("Should show correct amount of weeks")
    @CsvSource({"10, 2 weken", "12, 2 weken", "13, 3 weken"})
    void shouldShowCorrectAmountOfWeeks(int duration, String expectedString) {
        createSubjectWithDuration(duration);

        assertEquals(expectedString, subject.getDurationString());
    }
}