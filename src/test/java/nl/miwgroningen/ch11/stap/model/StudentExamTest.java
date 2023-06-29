package nl.miwgroningen.ch11.stap.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Thijs Harleman
 * Created at 09:49 on 26 Jun 2023
 * Purpose: tests if setGrade works correctly
 */
class StudentExamTest {
    private static final int NUMBER_OF_QUESTIONS = 4;

    private static final int AVERAGE_ATTAINABLE_POINTS = 2;
    private static final int MAX_ATTAINABLE_POINTS = 4;
    private static final int MIN_ATTAINABLE_POINTS = 0;

    private static final double AVERAGE_GRADE = 5.5;
    private static final int MAXIMUM_GRADE = 10;
    private static final int MINIMUM_GRADE = 1;

    List<StudentExamQuestion> studentExamQuestions;
    StudentExam studentExam;

    @BeforeEach
    void setup() {
        List<ExamQuestion> examQuestions = new ArrayList<>();
        studentExamQuestions = new ArrayList<>();

        for (int question = 0; question < NUMBER_OF_QUESTIONS; question++) {
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setAttainablePoints(MAX_ATTAINABLE_POINTS);
            examQuestions.add(examQuestion);

            studentExamQuestions.add(new StudentExamQuestion());
        }

        Exam exam = Exam.builder().examQuestions(examQuestions).build();

        studentExam = StudentExam.builder().exam(exam).studentExamQuestions(studentExamQuestions).build();
    }

    @Test
    @DisplayName("Should set grade to maximum when max points per answered question")
    void shouldSetGradeToMaximumWhenMaxPointsPerAnsweredQuestion() {
        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            studentExamQuestion.setPointsAttained(MAX_ATTAINABLE_POINTS);
        }
        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(MAXIMUM_GRADE, studentExam.getGrade());
    }

    @Test
    @DisplayName("Should set grade to minimum when min points per answered question")
    void shouldSetGradeToMinimumWhenMinPointsPerAnsweredQuestion() {
        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            studentExamQuestion.setPointsAttained(MIN_ATTAINABLE_POINTS);
        }

        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(MINIMUM_GRADE, studentExam.getGrade());
    }

    @Test
    @DisplayName("Should set grade to average when half points per answered question")
    void shouldSetGradeToAverageWhenHalfPointsPerAnsweredQuestion() {
        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            studentExamQuestion.setPointsAttained(AVERAGE_ATTAINABLE_POINTS);
        }

        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(AVERAGE_GRADE, studentExam.getGrade());
    }

    @Test
    @DisplayName("Should set grade to 1 when less than minimum points is entered")
    void shouldSetGradeTo1WhenLessThan0PointsIsEntered() {
        studentExamQuestions.get(0).setPointsAttained(MIN_ATTAINABLE_POINTS - 1);

        for (int question = 1; question < NUMBER_OF_QUESTIONS; question++) {
            studentExamQuestions.get(question).setPointsAttained(MIN_ATTAINABLE_POINTS);
        }

        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(MINIMUM_GRADE, studentExam.getGrade());
    }

    @Test
    @DisplayName("Should set grade to 10 when more than maximum points is entered")
    void shouldSetGradeTo10WhenMoreThanMaximumPointsIsEntered() {
        studentExamQuestions.get(0).setPointsAttained(MAX_ATTAINABLE_POINTS + 1);

        for (int question = 1; question < NUMBER_OF_QUESTIONS; question++) {
            studentExamQuestions.get(question).setPointsAttained(MAX_ATTAINABLE_POINTS);
        }

        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(MAXIMUM_GRADE, studentExam.getGrade());
    }
}