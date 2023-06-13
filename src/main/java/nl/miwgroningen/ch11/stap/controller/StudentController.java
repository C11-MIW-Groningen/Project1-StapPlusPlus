package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Student;
import nl.miwgroningen.ch11.stap.repositories.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tristan Meinsma
 * This handles interaction on the webpage about students
 */
@Controller
@RequestMapping("/student")
@RequiredArgsConstructor

public class StudentController {
    private final StudentRepository studentRepository;

    @GetMapping("/all")
    private String showStudentOverview(Model model) {
        model.addAttribute("allStudents", studentRepository.findAll());
        model.addAttribute("newStudent", new Student());
        return "studentOverview";
    }

    @PostMapping("/new")
    private String saveOrUpdateTeacher(@ModelAttribute("newStudent") Student student, BindingResult result) {
        if (!result.hasErrors()) {
            studentRepository.save(student);
        }

        return "redirect:/student/all";
    }
}
