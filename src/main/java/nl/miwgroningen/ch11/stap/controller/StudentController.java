package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Student;
import nl.miwgroningen.ch11.stap.model.StudentExam;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.StudentExamQuestionRepository;
import nl.miwgroningen.ch11.stap.repositories.StudentExamRepository;
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
    private final StudentExamRepository studentExamRepository;
    private final StudentExamQuestionRepository studentExamQuestionRepository;

    @GetMapping("/all")
    private String showStudentOverview(Model model) {
        List<Student> students = studentRepository.findAll();
        Collections.sort(students);
        model.addAttribute("allStudents", students);
        model.addAttribute("newStudent", new Student());
        return "student/studentOverview";
    }

    @GetMapping("/new")
    private String showStudentForm(Model model) {
        model.addAttribute("student", new Student());

        return "student/studentForm";
    }

    @GetMapping("/edit/{studentId}")
    private String showEditStudentForm(@PathVariable("studentId") Long studentId, Model model) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isPresent()) {
            model.addAttribute("student", optionalStudent.get());
            return "student/studentForm";
        }

        return "redirect:/student/all";
    }

    @PostMapping("/new")
    private String saveOrUpdateStudent(@ModelAttribute("newStudent") Student student, BindingResult result) {

        if (!result.hasErrors()) {
            studentRepository.save(student);
        } else {
            System.out.println(result.getAllErrors());
        }

        return "redirect:/student/all";
    }

    @GetMapping("/delete/{studentId}")
    private String deleteStudent(@PathVariable("studentId") Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isPresent()) {
            for (Cohort cohort : optionalStudent.get().getCohorts()) {
                cohort.removeStudent(optionalStudent.get());
                cohortRepository.save(cohort);
            }

            for (StudentExam studentExam : optionalStudent.get().getStudentExams()) {
                studentExamQuestionRepository.deleteAll(studentExam.getStudentExamQuestions());
                studentExamRepository.delete(studentExam);
            }

            studentRepository.deleteById(studentId);
        }

        return "redirect:/student/all";
    }

    @GetMapping("/cancel/{studentId}")
    private String cancelStudentEdit(@PathVariable("studentId") Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isPresent()) {
            List<Cohort> cohorts = optionalStudent.get().getCohorts();
            if ((cohorts.size()) > 0) {
                Cohort mostRecentCohort = cohorts.get(cohorts.size() - 1);
                return String.format("redirect:/cohort/details/%s", mostRecentCohort.getNumber());
            }
        }
        return "redirect:/student/all";
    }
}
