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
    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamQuestionRepository studentExamQuestionRepository;

    @GetMapping("/new/{examId}")
    private String showStudentExamForm(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            StudentExam studentExam = new StudentExam();
            studentExam.setExam(optionalExam.get());
            studentExamRepository.save(studentExam);
            addStudentExamQuestions(studentExam);

            model.addAttribute("studentExam", studentExam);

            return "exam/studentExamForm";
        }

        return String.format("redirect:/exam/details/%d", examId);
    }

    @GetMapping("/edit/{studentExamId}")
    private String showEditStudentExamForm(@PathVariable("studentExamId") Long studentExamId, Model model) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            model.addAttribute("studentExam", optionalStudentExam.get());

            return "exam/studentExamForm";
        }

        return "redirect:/exam/all";
    }

    @PostMapping("/new")
    private String saveStudentExam(@ModelAttribute("studentExam") StudentExam studentExamToSave, BindingResult result) {
        if (!result.hasErrors()) {
            studentExamQuestionRepository.saveAll(studentExamToSave.getStudentExamQuestions());
            studentExamToSave.setPointsAttained();
            studentExamToSave.setGrade();
            studentExamRepository.save(studentExamToSave);
        }

        return String.format("redirect:/exam/results/%d", studentExamToSave.getExam().getExamId());
    }

    @GetMapping("/delete/{studentExamId}")
    private String deleteExam(@PathVariable("studentExamId") Long studentExamId) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            Exam exam = optionalStudentExam.get().getExam();

            studentExamQuestionRepository.deleteAll(optionalStudentExam.get().getStudentExamQuestions());
            studentExamRepository.delete(optionalStudentExam.get());

            return String.format("redirect:/exam/results/%d", exam.getExamId());
        }

        return "redirect:/exam/all";
    }

    @GetMapping("/cancel/{studentExamId}")
    private String cancelStudentExam(@PathVariable("studentExamId") Long studentExamId) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            Exam exam = optionalStudentExam.get().getExam();

            if (optionalStudentExam.get().getStudent() == null) {
                studentExamRepository.delete(optionalStudentExam.get());
            }

            return String.format("redirect:/exam/results/%d", exam.getExamId());
        }

        return "redirect:/exam/all";
    }

    private void addStudentExamQuestions(StudentExam studentExam) {
        for (ExamQuestion examQuestion : studentExam.getExam().getExamQuestions()) {
            StudentExamQuestion studentExamQuestion = new StudentExamQuestion();
            studentExamQuestion.setQuestionNumber(examQuestion.getQuestionNumber());
            studentExamQuestion.setStudentExam(studentExam);
            studentExam.getStudentExamQuestions().add(studentExamQuestion);
        }

        studentExamRepository.save(studentExam);
    }
}
