package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Subject;
import nl.miwgroningen.ch11.stap.repositories.LearningGoalRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tristan Meinsma
 * This handles interaction with webpage about subjects
 */

@Controller
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectRepository subjectRepository;
    private final LearningGoalRepository learningGoalRepository;

    @GetMapping("/all")
    private String showSubjectOverview(Model model) {
        model.addAttribute("allSubjects", subjectRepository.findAll());
        return "subjectOverview";
    }

    @GetMapping("/new")
    private String showSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("allLearningGoals", learningGoalRepository.findAll());

        return "subjectForm";
    }

    @PostMapping("/new")
    private String saveSubject(@ModelAttribute("subject") Subject subjectToSave, BindingResult result) {
        if (!result.hasErrors()) {
            subjectRepository.save(subjectToSave);
        }

        return "redirect:/subject/all";
    }
}

