package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.dto.EnrollmentDTO;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Exam;
import nl.miwgroningen.ch11.stap.model.Student;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.CourseRepository;
import nl.miwgroningen.ch11.stap.repositories.ExamRepository;
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
    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;

    @GetMapping("/all")
    private String showCohortOverview(Model model) {
        model.addAttribute("allCohorts", cohortRepository.findAll());
        return "cohort/cohortOverview";
    }

    @GetMapping("/new")
    private String showCohortForm(Model model) {
        model.addAttribute("cohort", new Cohort());

        model.addAttribute("allCourses", courseRepository.findAll());
        model.addAttribute("allStudents", studentRepository.findAll());

        return "cohort/cohortForm";
    }

    @GetMapping("/edit/{cohortId}")
    private String showEditCohortForm(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());

            List<Student> allStudents = getStudentsSorted();
            List<Student> studentFromThisCohort = optionalCohort.get().getStudents();
            allStudents.removeAll(studentFromThisCohort);

            model.addAttribute("allStudents", allStudents);
            model.addAttribute("allCourses", courseRepository.findAll());

            return "cohort/cohortForm";
        }

        return "redirect:/cohort/all";
    }

    @GetMapping("/details/{number}")
    public String showCohortDetails(@PathVariable("number") String number, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findByNumber(number);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());
            List<Student> unenrolledStudents = studentRepository.findAll();
            unenrolledStudents.removeAll(optionalCohort.get().getStudents());
            model.addAttribute("allUnenrolledStudents", unenrolledStudents);
            model.addAttribute("enrollment", EnrollmentDTO.builder().cohort(optionalCohort.get()).build());
            return "cohort/cohortDetails";
        }

        return "redirect:/cohort/all";
    }

    @PostMapping("/enrollStudent")
    public String enrollStudent(@ModelAttribute("enrollment")
                                EnrollmentDTO enrollment, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/cohort/all";
        }

        enrollment.getCohort().addStudent(enrollment.getStudent());
        cohortRepository.save(enrollment.getCohort());

        return "redirect:/cohort/details/" + enrollment.getCohort().getNumber();
    }

    @GetMapping("/unenroll/{cohortId}/{studentId}")
    public String unenrollStudent(@PathVariable("cohortId") Long cohortId,
                                  @PathVariable("studentId") Long studentId) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalCohort.isEmpty() || optionalStudent.isEmpty()) {
            return "redirect:/cohort/all";
        }

        optionalCohort.get().removeStudent(optionalStudent.get());
        cohortRepository.save(optionalCohort.get());

        return "redirect:/cohort/details/" + optionalCohort.get().getNumber();
    }


    @PostMapping("/new")
    private String saveOrUpdateCohort(@ModelAttribute("newCohort") Cohort cohort, BindingResult result) {

        if (!result.hasErrors()) {
            cohortRepository.save(cohort);
        }
        return "redirect:/cohort/all";
    }



    @GetMapping("/delete/{cohortId}")
    private String deleteCohort(@PathVariable("cohortId") Long cohortId) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            for (Exam exam : optionalCohort.get().getExams()) {
                exam.removeCohort();
                examRepository.save(exam);
            }
            cohortRepository.deleteById(cohortId);
        }

        return "redirect:/cohort/all";
    }

    private List<Student> getStudentsSorted() {
        List<Student> allStudents = studentRepository.findAll();
        Collections.sort(allStudents);
        return allStudents;
    }
}
