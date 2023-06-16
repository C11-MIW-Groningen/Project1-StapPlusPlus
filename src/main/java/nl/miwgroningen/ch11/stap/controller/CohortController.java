package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Student;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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
        return "redirect:/cohort/all";
    }

    @GetMapping("/edit/{cohortId}")
    private String showEditCohortForm(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());
            model.addAttribute("allStudents", getStudentsSorted());
            return "cohortForm";
        }

        return "redirect:/cohort/all";
    }

    @PostMapping("/new")
    private String saveOrUpdateCohort(@ModelAttribute("newCohort") Cohort cohort, BindingResult result) {
        System.out.println(cohort.getStartDate());
        System.out.println(cohort.getNumber());
        if (!result.hasErrors()) {
            cohortRepository.save(cohort);
        }
        return "redirect:/cohort/all";
    }

    @GetMapping("/details/{number}")
    private String showCohortDetails(@PathVariable("number") String number, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findByNumber(number);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohortShown", optionalCohort.get());

            return "cohortDetails";
        }

        return "redirect:/cohort/all";
    }

    @GetMapping("/delete/{cohortId}")
    private String deleteCohort(@PathVariable("cohortId") Long cohortId) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        optionalCohort.ifPresent(cohortRepository::delete);

        return "redirect:/cohort/all";
    }

    private List<Student> getStudentsSorted() {
        List<Student> allStudents = studentRepository.findAll();
        Collections.sort(allStudents);
        return allStudents;
    }
}
