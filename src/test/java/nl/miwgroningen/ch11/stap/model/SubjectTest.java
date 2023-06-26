package nl.miwgroningen.ch11.stap.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tristan Meinsma
 * Tests if duration is displayed correctly
 */
class SubjectTest {

    private Subject createSubjectWithDuration(int duration) {
        Subject subject = new Subject();
        subject.setDuration(duration);
        return subject;
    }

    @Test
    @DisplayName("Should return 1 day")
    void shouldReturn1Day() {
        Subject subject = createSubjectWithDuration(1);
        String expectedString = "1 dag";

        assertEquals(expectedString, subject.getDurationString());
    }

    @ParameterizedTest
    @DisplayName("Should show the correct amount of days")
    @ValueSource(ints = {2, 5, 9})
    void shouldShowTheCorrectAmountOfDays(int expectedNumberOfDays) {
        Subject subject = createSubjectWithDuration(expectedNumberOfDays);
        String expectedString = expectedNumberOfDays + " dagen";

        assertEquals(expectedString, subject.getDurationString());
    }

    @Test
    @DisplayName("Should show 2 weeks")
    void shouldShow2Weeks() {
        Subject subject1 = createSubjectWithDuration(10);
        String expectedString = "2 weken";

        Subject subject2 = createSubjectWithDuration(12);
        String expectedString1 = "2 weken";

        assertAll(() -> assertEquals(expectedString, subject1.getDurationString()),
                () -> assertEquals(expectedString1, subject2.getDurationString()));
    }

    @Test
    @DisplayName("Should show 3 weeks")
    void shouldShow3Weeks() {
        Subject subject1 = createSubjectWithDuration(13);
        String expectedString = "3 weken";

        Subject subject2 = createSubjectWithDuration(17);
        String expectedString1 = "3 weken";

        assertAll(() -> assertEquals(expectedString, subject1.getDurationString()),
                () -> assertEquals(expectedString1, subject2.getDurationString()));
    }
}