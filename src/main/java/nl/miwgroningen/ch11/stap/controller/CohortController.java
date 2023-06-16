package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.StudentRepository;
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
    private final StudentRepository studentRepository;

    @GetMapping("/all")
    private String showCohortOverview(Model model) {
        model.addAttribute("allCohorts", cohortRepository.findAll());
        return "cohortOverview";
    }

    @GetMapping("/new")
    private String showCohortForm(Model model) {
        model.addAttribute("cohort", new Cohort());

        return "cohortForm";
    }

    @GetMapping("/cohort/details/{cohortId}")
    private String showCohortDetails(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);
        if (optionalCohort.isPresent()) {
            model.addAttribute("shownCohort", optionalCohort.get());

            return "cohortDetails";
        }
        return "redirect:/cohort";
    }

    @GetMapping("/edit/{cohortId}")
    private String showEditCohortForm(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());
            model.addAttribute("allStudents", studentRepository.findAll());
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

    @GetMapping("/details/{number}")
    private String showCohortDetails(@PathVariable("number") String number, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findByNumber(number);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohortShown", optionalCohort.get());

            return "cohortDetails";
        }

        return "redirect:/cohort/";
    }
}
