package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Exam;
import nl.miwgroningen.ch11.stap.model.ExamQuestion;
import nl.miwgroningen.ch11.stap.repositories.ExamQuestionRepository;
import nl.miwgroningen.ch11.stap.repositories.ExamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Author: Thijs Harleman
 * Created at 10:44 on 19 Jun 2023
 * Purpose: Handle interactions with exam questions.
 */

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class ExamQuestionController {
    private static final String REDIRECT_EXAM_OVERVIEW = "redirect:/exam/all";

    private final ExamQuestionRepository examQuestionRepository;
    private final ExamRepository examRepository;

    @GetMapping("/new/{examId}")
    public String showExamQuestionForm(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setExam(optionalExam.get());
            examQuestionRepository.save(examQuestion);
            model.addAttribute("examQuestion", examQuestion);
        }

        return "exam/examQuestionForm";
    }

    @GetMapping("/edit/{examQuestionId}")
    public String showEditExamQuestionForm(@PathVariable("examQuestionId") Long examQuestionId, Model model) {
        Optional<ExamQuestion> optionalExamQuestion = examQuestionRepository.findById(examQuestionId);

        if (optionalExamQuestion.isPresent()) {
            model.addAttribute("examQuestion", optionalExamQuestion.get());

            return "exam/examQuestionForm";
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @PostMapping("/new")
    public String saveExamQuestion(@ModelAttribute("examQuestion") ExamQuestion examQuestionToSave,
                                    BindingResult result) {
        if (!result.hasErrors()) {
            examQuestionRepository.save(examQuestionToSave);
            examRepository.save(examQuestionToSave.getExam());

            return String.format("redirect:/exam/details/%d", examQuestionToSave.getExam().getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/delete/{questionId}")
    public String deleteExamQuestion(@PathVariable("questionId") Long questionId) {
        Optional<ExamQuestion> optionalExamQuestion = examQuestionRepository.findById(questionId);

        if (optionalExamQuestion.isPresent()) {
            Exam exam = optionalExamQuestion.get().getExam();
            examQuestionRepository.delete(optionalExamQuestion.get());

            return String.format("redirect:/exam/details/%d", exam.getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }
}
