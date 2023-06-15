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
 * This handles interaction on the webpage about students
 */
@Controller
@RequestMapping("/student")
@RequiredArgsConstructor

public class StudentController {
    private final StudentRepository studentRepository;
    private final CohortRepository cohortRepository;

    @GetMapping("/all")
    private String showStudentOverview(Model model) {
        List<Student> students = studentRepository.findAll();
        Collections.sort(students);
        model.addAttribute("allStudents", students);
        model.addAttribute("newStudent", new Student());
        return "studentOverview";
    }

    @GetMapping("/new")
    private String showStudentForm(Model model) {
        model.addAttribute("student", new Student());

        return "studentForm";
    }

    @GetMapping("/edit/{studentId}")
    private String showEditStudentForm(@PathVariable("studentId") Long studentId, Model model) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isPresent()) {
            model.addAttribute("student", optionalStudent.get());
            model.addAttribute("allCohorts", cohortRepository.findAll());
            return "studentForm";
        }

        return "redirect:/student/all";
    }

    @PostMapping("/new")
    private String saveOrUpdateStudent(@ModelAttribute("newStudent") Student student, BindingResult result) {
        if (!result.hasErrors()) {
            studentRepository.save(student);
        }

        return "redirect:/student/all";
    }

    @GetMapping("/delete/{studentId}")
    private String deleteStudent(@PathVariable("studentId") Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isPresent()) {
            studentRepository.delete(optionalStudent.get());
        }

        return "redirect:/student/all";
    }
}
