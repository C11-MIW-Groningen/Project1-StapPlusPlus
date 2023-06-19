package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Exam;
import nl.miwgroningen.ch11.stap.model.ExamQuestion;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.ExamQuestionRepository;
import nl.miwgroningen.ch11.stap.repositories.ExamRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
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
    private final CohortRepository cohortRepository;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/all")
    private String showExamOverview(Model model) {
        List<Exam> allExams = examRepository.findAll();
        Collections.sort(allExams);
        model.addAttribute("allExams", allExams);
        return "examOverview";
    }

    @GetMapping("/new")
    private String showExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        model.addAttribute("allCohorts", cohortRepository.findAll());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return "examForm";
    }

    @GetMapping("/edit/{examId}")
    private String showEditExamForm(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            model.addAttribute("exam", optionalExam.get());
            model.addAttribute("allCohorts", cohortRepository.findAll());
            model.addAttribute("allSubjects", subjectRepository.findAll());

            return "examForm";
        }

        return "redirect:/exam/all";
    }

    @PostMapping("/new")
    private String saveExam(@ModelAttribute("exam") Exam examToSave, BindingResult result) {
        if (!result.hasErrors()) {
            examRepository.save(examToSave);
        }

        return "redirect:/exam/all";
    }

    @GetMapping("/details/{examId}")
    private String showExamDetails(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            model.addAttribute("shownExam", optionalExam.get());

            return "examDetails";
        }

        return "redirect:/exam/all";
    }

    @GetMapping("/cancel/{examId}")
    private String cancelNewQuestion(@PathVariable("examId") Long examId) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            List<ExamQuestion> examQuestions = optionalExam.get().getExamQuestions();
            examQuestionRepository.delete(examQuestions.get(examQuestions.size() - 1));

            return String.format("redirect:/exam/details/%d", examId);
        }

        return "redirect:/exam/all";
    }
}
