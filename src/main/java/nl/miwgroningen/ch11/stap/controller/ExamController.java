package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Exam;
import nl.miwgroningen.ch11.stap.model.ExamQuestion;
import nl.miwgroningen.ch11.stap.model.StudentExam;
import nl.miwgroningen.ch11.stap.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Author: Thijs Harleman
 * Created at 09:46 on 19 Jun 2023
 * Purpose: Handle interactions with exams.
 */

@Controller
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {
    private static final String REDIRECT_EXAM_DETAILS = "redirect:/exam/details/%d";
    private static final String REDIRECT_EXAM_OVERVIEW = "redirect:/exam/all";
    private static final String VIEW_EXAM_DETAILS = "exam/examDetails";
    private static final String VIEW_EXAM_FORM = "exam/examForm";
    private static final String VIEW_EXAM_OVERVIEW = "exam/examOverview";
    private static final String VIEW_EXAM_RESULTS = "exam/examResults";

    private final CohortRepository cohortRepository;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamQuestionRepository studentExamQuestionRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/all")
    public String showExamOverview(Model model) {
        List<Exam> allExams = examRepository.findAll();
        Collections.sort(allExams);
        model.addAttribute("allExams", allExams);

        return VIEW_EXAM_OVERVIEW;
    }

    @GetMapping("/new")
    public String showExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        model.addAttribute("allCohorts", cohortRepository.findAll());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return VIEW_EXAM_FORM;
    }

    @GetMapping("/edit/{examId}")
    public String showEditExamForm(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            model.addAttribute("exam", optionalExam.get());
            model.addAttribute("allCohorts", cohortRepository.findAll());
            model.addAttribute("allSubjects", subjectRepository.findAll());

            return VIEW_EXAM_FORM;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @PostMapping("/new")
    public String saveExam(@ModelAttribute("exam") Exam examToSave, BindingResult result) {
        if (!result.hasErrors()) {
            examRepository.save(examToSave);

            return String.format(REDIRECT_EXAM_DETAILS, examToSave.getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/delete/{examId}")
    public String deleteExam(@PathVariable("examId") Long examId) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            for (StudentExam studentExam : optionalExam.get().getStudentExams()) {
                studentExamQuestionRepository.deleteAll(studentExam.getStudentExamQuestions());
                studentExamRepository.delete(studentExam);
            }

            examQuestionRepository.deleteAll(optionalExam.get().getExamQuestions());
            examRepository.delete(optionalExam.get());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/details/{examId}")
    public String showExamDetails(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            model.addAttribute("shownExam", optionalExam.get());

            return VIEW_EXAM_DETAILS;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/results/{examId}")
    public String showExamResults(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            List<StudentExam> studentExams = optionalExam.get().getStudentExams();
            Collections.sort(studentExams);

            model.addAttribute("shownExam", optionalExam.get());
            model.addAttribute("shownStudentExams", studentExams);

            return VIEW_EXAM_RESULTS;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/cancel/{examId}")
    public String cancelNewQuestion(@PathVariable("examId") Long examId) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            List<ExamQuestion> examQuestions = optionalExam.get().getExamQuestions();
            examQuestionRepository.delete(examQuestions.get(examQuestions.size() - 1));

            return String.format(REDIRECT_EXAM_DETAILS, examId);
        }

        return REDIRECT_EXAM_OVERVIEW;
    }
}
