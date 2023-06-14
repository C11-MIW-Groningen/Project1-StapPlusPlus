package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tristan Meinsma
 * Dit programma doet x
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


}
