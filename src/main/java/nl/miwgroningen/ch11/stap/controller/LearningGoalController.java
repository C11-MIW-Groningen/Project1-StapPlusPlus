package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.LearningGoal;
import nl.miwgroningen.ch11.stap.repositories.LearningGoalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

        return "learningGoal/learningGoalOverview";
    }

    @GetMapping("/new")
    private String showLearningGoalForm(Model model) {
        model.addAttribute("learningGoal", new LearningGoal());

        return "learningGoal/learningGoalForm";
    }

    @GetMapping("/edit/{learningGoalId}")
    private String showEditLearningGoalForm(@PathVariable("learningGoalId") Long goalId, Model model) {
        Optional<LearningGoal> optionalLearningGoal = learningGoalRepository.findById(goalId);

        if (optionalLearningGoal.isPresent()) {
            model.addAttribute("learningGoal", optionalLearningGoal.get());

            return "learningGoal/learningGoalForm";
        }

        return "redirect:/goal/all";
    }

    @PostMapping("/new")
    private String saveLearningGoal(@ModelAttribute("learningGoal") LearningGoal goalToSave, BindingResult result) {
        if (!result.hasErrors()) {
            learningGoalRepository.save(goalToSave);
        }

        return "redirect:/goal/all";
    }

    @GetMapping("/delete/{learningGoalId}")
    private String deleteAuthor(@PathVariable("learningGoalId") Long goalId) {
        Optional<LearningGoal> optionalLearningGoal = learningGoalRepository.findById(goalId);

        optionalLearningGoal.ifPresent(learningGoalRepository::delete);

        return "redirect:/goal/all";
    }
}
