package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Student;
import nl.miwgroningen.ch11.stap.model.StudentExam;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.StudentExamAnswerRepository;
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
    private static final String STUDENT_OVERVIEW = "student/studentOverview";
    private static final String REDIRECT_STUDENT_ALL = "redirect:/student/all";
    private static final String STUDENT_FORM = "student/studentForm";


    private final StudentRepository studentRepository;
    private final CohortRepository cohortRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamAnswerRepository studentExamAnswerRepository;

    @GetMapping("/all")
    private String showStudentOverview(Model model) {
        List<Student> students = studentRepository.findAll();
        Collections.sort(students);

        model.addAttribute("allStudents", students);
        model.addAttribute("newStudent", new Student());

        return STUDENT_OVERVIEW;
    }

    @GetMapping("/new")
    private String showStudentForm(Model model) {
        model.addAttribute("student", new Student());

        return STUDENT_FORM;
    }

    @GetMapping("/edit/{studentId}")
    private String showEditStudentForm(@PathVariable("studentId") Long studentId, Model model) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isPresent()) {
            model.addAttribute("student", optionalStudent.get());
            return STUDENT_FORM;
        }

        return REDIRECT_STUDENT_ALL;
    }

    @PostMapping("/new")
    private String saveOrUpdateStudent(@ModelAttribute("newStudent") Student student, BindingResult result) {

        if (!result.hasErrors()) {
            studentRepository.save(student);
        } else {
            System.out.println(result.getAllErrors());
        }

        return REDIRECT_STUDENT_ALL;
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
                studentExamAnswerRepository.deleteAll(studentExam.getStudentExamAnswers());
                studentExamRepository.delete(studentExam);
            }

            studentRepository.deleteById(studentId);
        }

        return REDIRECT_STUDENT_ALL;
    }
}
