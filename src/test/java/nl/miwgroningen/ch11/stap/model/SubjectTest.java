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

    @ParameterizedTest
    @DisplayName("Should show the correct amount of days")
    @ValueSource(ints = {1, 5, 9})
    void shouldShowTheCorrectAmountOfDays(int expectedNumberOfDays) {
        Subject subject = new Subject();
        subject.setDuration(expectedNumberOfDays);

        assertEquals(String.format(expectedNumberOfDays + " dagen"), subject.getDurationString());
    }

    @Test
    @DisplayName("should show 2 weeks")
    void shouldShow2Weeks() {
        Subject subject = new Subject();
        subject.setDuration(10);
        String expectedString = "2 weken";

        Subject subject1 = new Subject();
        subject1.setDuration(12);
        String expectedString1 = "2 weken";

        assertAll(() -> assertEquals(expectedString, subject.getDurationString()),
                () -> assertEquals(expectedString1, subject1.getDurationString()));
    }

    @Test
    @DisplayName("should show 3 weeks")
    void shouldShow3Weeks() {
        Subject subject = new Subject();
        subject.setDuration(13);
        String expectedString = "3 weken";

        Subject subject1 = new Subject();
        subject1.setDuration(17);
        String expectedString1 = "3 weken";

        assertAll(() -> assertEquals(expectedString, subject.getDurationString()),
                () -> assertEquals(expectedString1, subject1.getDurationString()));
    }
}