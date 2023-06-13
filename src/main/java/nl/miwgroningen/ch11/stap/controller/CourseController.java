package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author Tristan Meinsma
 * This handles interaction on webpage about courses
 */

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

    @GetMapping("/")
    private String showSubjectOverview(Model model) {
        model.addAttribute("allCourses", courseRepository.findAll());
        return "courseOverview";
    }
}
