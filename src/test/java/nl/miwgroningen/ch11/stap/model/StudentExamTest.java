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
 * Purpose:
 */
class StudentExamTest {
    List<StudentExamQuestion> studentExamQuestions;
    StudentExam studentExam;

    @BeforeEach
    void setup() {
        List<ExamQuestion> examQuestions = new ArrayList<>();
        studentExamQuestions = new ArrayList<>();

        for (int question = 0; question < 4; question++) {
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setAttainablePoints(5);
            examQuestions.add(examQuestion);

            studentExamQuestions.add(new StudentExamQuestion());
        }

        Exam exam = Exam.builder().examQuestions(examQuestions).build();

        studentExam = StudentExam.builder().exam(exam).studentExamQuestions(studentExamQuestions).build();
    }

    @Test
    @DisplayName("Should set grade to 10 when 5 points per answered question")
    void shouldSetGradeTo10When5PointsPerAnsweredQuestion() {
        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            studentExamQuestion.setPointsAttained(5);
        }
        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(10, studentExam.getGrade());
    }

    @Test
    @DisplayName("Should set grade to 1 when 0 points per answered question")
    void shouldSetGradeTo1When0PointsPerAnsweredQuestion() {
        for (StudentExamQuestion studentExamQuestion : studentExamQuestions) {
            studentExamQuestion.setPointsAttained(0);
        }

        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(1, studentExam.getGrade());
    }

    @Test
    @DisplayName("Should set grade to 5.5 when 2 or 3 points per answered question")
    void shouldSetGradeTo5Point5When2Or3PointsPerAnsweredQuestion() {
        for (int question = 0; question < 2; question++) {
            studentExamQuestions.get(question).setPointsAttained(2);
        }

        for (int question = 2; question < 4; question++) {
            studentExamQuestions.get(question).setPointsAttained(3);
        }

        studentExam.setPointsAttained();
        studentExam.setGrade();

        assertEquals(5.5, studentExam.getGrade());
    }
}