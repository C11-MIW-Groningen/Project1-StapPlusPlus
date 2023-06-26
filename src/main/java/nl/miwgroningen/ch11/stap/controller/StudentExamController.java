package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Exam;
import nl.miwgroningen.ch11.stap.model.ExamQuestion;
import nl.miwgroningen.ch11.stap.model.StudentExam;
import nl.miwgroningen.ch11.stap.model.StudentExamQuestion;
import nl.miwgroningen.ch11.stap.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Author: Thijs Harleman
 * Created at 09:46 on 19 Jun 2023
 * Purpose: Handle interactions with student responses for exams.
 */

@Controller
@RequestMapping("/studentExam")
@RequiredArgsConstructor
public class StudentExamController {
    private static final String REDIRECT_EXAM_DETAILS = "redirect:/exam/details/%d";
    private static final String REDIRECT_EXAM_OVERVIEW = "redirect:/exam/all";
    private static final String REDIRECT_EXAM_RESULTS = "redirect:/exam/results/%d";
    private static final String STUDENT_EXAM_FORM = "exam/studentExamForm";

    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamQuestionRepository studentExamQuestionRepository;

    @GetMapping("/new/{examId}")
    public String showStudentExamForm(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            StudentExam studentExam = new StudentExam();
            studentExam.setExam(optionalExam.get());
            studentExamRepository.save(studentExam);
            addStudentExamQuestions(studentExam);

            model.addAttribute("studentExam", studentExam);

            return STUDENT_EXAM_FORM;
        }

        return String.format(REDIRECT_EXAM_DETAILS, examId);
    }

    @GetMapping("/edit/{studentExamId}")
    public String showEditStudentExamForm(@PathVariable("studentExamId") Long studentExamId, Model model) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            model.addAttribute("studentExam", optionalStudentExam.get());

            return STUDENT_EXAM_FORM;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @PostMapping("/new")
    public String saveStudentExam(@ModelAttribute("studentExam") StudentExam studentExamToSave, BindingResult result) {
        if (!result.hasErrors()) {
            studentExamQuestionRepository.saveAll(studentExamToSave.getStudentExamQuestions());
            studentExamToSave.setPointsAttained();
            studentExamToSave.setGrade();
            studentExamRepository.save(studentExamToSave);
        }

        return String.format(REDIRECT_EXAM_RESULTS, studentExamToSave.getExam().getExamId());
    }

    @GetMapping("/delete/{studentExamId}")
    public String deleteExam(@PathVariable("studentExamId") Long studentExamId) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            Exam exam = optionalStudentExam.get().getExam();

            studentExamQuestionRepository.deleteAll(optionalStudentExam.get().getStudentExamQuestions());
            studentExamRepository.delete(optionalStudentExam.get());

            return String.format(REDIRECT_EXAM_RESULTS, exam.getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/cancel/{studentExamId}")
    public String cancelStudentExam(@PathVariable("studentExamId") Long studentExamId) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            Exam exam = optionalStudentExam.get().getExam();

            if (optionalStudentExam.get().getStudent() == null) {
                for (StudentExamQuestion studentExamQuestion : optionalStudentExam.get().getStudentExamQuestions()) {
                    studentExamQuestionRepository.deleteAll(
                            studentExamQuestion.getStudentExam().getStudentExamQuestions());
                }
                studentExamRepository.delete(optionalStudentExam.get());
            }

            return String.format(REDIRECT_EXAM_RESULTS, exam.getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    private void addStudentExamQuestions(StudentExam studentExam) {
        for (ExamQuestion examQuestion : studentExam.getExam().getExamQuestions()) {
            StudentExamQuestion studentExamQuestion = new StudentExamQuestion();
            studentExamQuestion.setQuestionNumber(examQuestion.getQuestionNumber());
            studentExamQuestion.setStudentExam(studentExam);
            studentExam.getStudentExamQuestions().add(studentExamQuestion);
            studentExamQuestionRepository.save(studentExamQuestion);
        }
    }
}
