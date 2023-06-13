package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.LearningGoal;
import nl.miwgroningen.ch11.stap.repositories.LearningGoalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: Thijs Harleman
 * Created at 09:17 on 13 Jun 2023
 * Purpose: handle interactions with learning goals
 */
@Controller
@RequestMapping("/goal")
@RequiredArgsConstructor
public class LearningGoalController {
    private final LearningGoalRepository learningGoalRepository;

    @GetMapping("/all")
    private String showLearningGoalOverview(Model model) {
        model.addAttribute("allLearningGoals", learningGoalRepository.findAll());

        return "learningGoalOverview";
    }

    @GetMapping("/new")
    private String showLearningGoalForm(Model model) {
        model.addAttribute("learningGoal", new LearningGoal());

        return "learningGoalForm";
    }

    @PostMapping("/new")
    private String saveLearningGoal(@ModelAttribute("learningGoal") LearningGoal goalToSave, BindingResult result) {
        if (!result.hasErrors()) {
            learningGoalRepository.save(goalToSave);
        }

        return "redirect:/goal/all";
    }
}
