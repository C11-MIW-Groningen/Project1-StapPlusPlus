package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Tristan Meinsma
 * Handles interaction on web about cohorts
 */
@Controller
@RequestMapping("/cohort")
@RequiredArgsConstructor
public class CohortController {
    private final CohortRepository cohortRepository;

    @GetMapping("/")
    private String showCohortOverview(Model model) {
        model.addAttribute("allCohorts", cohortRepository.findAll());
        return "cohortOverview";
    }

    @GetMapping("/new")
    private String showCohortForm(Model model) {
        model.addAttribute("cohort", new Cohort());

        return "cohortForm";
    }

    @GetMapping("/edit/{cohortId}")
    private String showEditCohortForm(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());
            model.addAttribute("allCohorts", cohortRepository.findAll());
            return "cohortForm";
        }

        return "redirect:/cohort/";
    }

    @PostMapping("/new")
    private String saveOrUpdateCohort(@ModelAttribute("newCohort") Cohort cohort, BindingResult result) {
        if (!result.hasErrors()) {
            cohortRepository.save(cohort);
        }
        return "redirect:/cohort/";
    }

    @GetMapping("/delete/{cohortId}")
    private String deleteCohort(@PathVariable("cohortId") Long cohortId) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            cohortRepository.delete(optionalCohort.get());
        }
        return "redirect:/cohort/";
    }
}
