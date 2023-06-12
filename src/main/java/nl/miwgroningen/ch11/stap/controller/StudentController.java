package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tristan Meinsma
 * This handles interaction on the webpage about students
 */
@Controller
@RequestMapping("/student")
@RequiredArgsConstructor

public class StudentController {

    @GetMapping("/all")
    private String showStudentOverview() {
        return "studentOverview";
    }
}
