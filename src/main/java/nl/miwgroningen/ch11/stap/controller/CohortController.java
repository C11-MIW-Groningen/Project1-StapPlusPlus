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
    private static final String COHORT_OVERVIEW = "cohort/cohortOverview";
    private static final String REDIRECT_COHORT_ALL = "redirect:/cohort/all";
    private static final String COHORT_FORM = "cohort/cohortForm";
    private static final String COHORT_DETAILS = "cohort/cohortDetails";
    private static final String REDIRECT_COHORT_DETAILS = "redirect:/cohort/details/";

    private final CohortRepository cohortRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;

    @GetMapping("/all")
    private String showCohortOverview(Model model) {
        model.addAttribute("allCohorts", cohortRepository.findAll());
        return COHORT_OVERVIEW;
    }

    @GetMapping("/new")
    private String showCohortForm(Model model) {
        model.addAttribute("cohort", new Cohort());
        model.addAttribute("allCourses", courseRepository.findAll());

        return COHORT_FORM;
    }

    @GetMapping("/edit/{cohortId}")
    private String showEditCohortForm(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());
            model.addAttribute("allCourses", courseRepository.findAll());

            return COHORT_FORM;
        }

        return REDIRECT_COHORT_ALL;
    }

    @GetMapping("/details/{cohortId}")
    public String showCohortDetails(@PathVariable("cohortId") Long cohortId, Model model) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);

        if (optionalCohort.isPresent()) {
            model.addAttribute("cohort", optionalCohort.get());

            List<Student> unenrolledStudents = getStudentsSorted();
            unenrolledStudents.removeAll(optionalCohort.get().getStudents());

            model.addAttribute("allUnenrolledStudents", unenrolledStudents);
            model.addAttribute("enrollment", EnrollmentDTO.builder().cohort(optionalCohort.get()).build());
            Collections.sort(optionalCohort.get().getStudents());

            return COHORT_DETAILS;
        }

        return REDIRECT_COHORT_ALL;
    }

    @PostMapping("/enrollStudent")
    public String enrollStudent(@ModelAttribute("enrollment") EnrollmentDTO enrollment, BindingResult result) {
        if (result.hasErrors()) {
            return REDIRECT_COHORT_ALL;
        }

        enrollment.getCohort().addStudent(enrollment.getStudent());
        cohortRepository.save(enrollment.getCohort());

        return REDIRECT_COHORT_DETAILS + enrollment.getCohort().getCohortId();
    }

    @GetMapping("/unenroll/{cohortId}/{studentId}")
    public String unenrollStudent(@PathVariable("cohortId") Long cohortId,
                                  @PathVariable("studentId") Long studentId) {
        Optional<Cohort> optionalCohort = cohortRepository.findById(cohortId);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalCohort.isEmpty() || optionalStudent.isEmpty()) {
            return REDIRECT_COHORT_ALL;
        }

        optionalCohort.get().removeStudent(optionalStudent.get());
        cohortRepository.save(optionalCohort.get());

        return REDIRECT_COHORT_DETAILS + optionalCohort.get().getCohortId();
    }

    @PostMapping("/new")
    private String saveOrUpdateCohort(@ModelAttribute("newCohort") Cohort cohort, BindingResult result) {
        if (!result.hasErrors()) {
            cohortRepository.save(cohort);
        }

        return REDIRECT_COHORT_ALL;
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

        return REDIRECT_COHORT_ALL;
    }

    private List<Student> getStudentsSorted() {
        List<Student> allStudents = studentRepository.findAll();
        Collections.sort(allStudents);
        return allStudents;
    }
}
