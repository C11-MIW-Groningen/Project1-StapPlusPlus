package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Course;
import nl.miwgroningen.ch11.stap.model.Subject;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.CourseRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * @author Tristan Meinsma
 * This handles interaction on webpage about courses
 */

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CohortRepository cohortRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/")
    private String showLandingPage(Model model) {
        model.addAttribute("allCourses", courseRepository.findAll());

        return "landingPage";
    }

    @GetMapping("/course/all")
    private String showCourseOverview(Model model) {
        model.addAttribute("allCourses", courseRepository.findAll());

        return "course/courseOverview";
    }

    @GetMapping("/course/details/{name}")
    private String showCourseDetails(@PathVariable("name") String name, Model model) {
        Optional<Course> optionalCourse = courseRepository.findByName(name);

        if (optionalCourse.isPresent()) {
            model.addAttribute("shownCourse", optionalCourse.get());

            return "course/courseDetails";
        }

        return "redirect:/";
    }

    @GetMapping("/course/new")
    private String showCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return "course/courseForm";
    }

    @GetMapping("/course/edit/{courseId}")
    private String showEditCourseForm(@PathVariable("courseId") Long courseId, Model model) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            model.addAttribute("course", optionalCourse.get());
            model.addAttribute("allSubjects", subjectRepository.findAll());

            return "course/courseForm";
        }

        return "redirect:/";
    }

    @PostMapping("/course/new")
    private String saveCourse(@ModelAttribute("course") Course courseToSave, BindingResult result) {
        if (!result.hasErrors()) {
            courseRepository.save(courseToSave);
        }

        return String.format("redirect:/course/details/%s",
                courseToSave.getName().replace(" ", "%20"));
    }

    @GetMapping("/course/delete/{courseId}")
    private String deleteCourse(@PathVariable("courseId") Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            for (Subject subject : optionalCourse.get().getSubjects()) {
                subject.removeCourse(optionalCourse.get());
                subjectRepository.save(subject);
            }

            for (Cohort cohort : optionalCourse.get().getCohorts()) {
                cohort.removeCourse();
                cohortRepository.save(cohort);
            }

            courseRepository.delete(optionalCourse.get());
        }

        return "redirect:/course/all";
    }

    @GetMapping("/course/remove/{courseId}/{subjectId}")
    private String removeSubject(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalCourse.isPresent()) {
            if (optionalSubject.isPresent()) {
                List<Subject> courseSubjects = optionalCourse.get().getSubjects();
                courseSubjects.remove(optionalSubject.get());
                optionalCourse.get().setSubjects(courseSubjects);
                courseRepository.save(optionalCourse.get());
            }
            return String.format("redirect:/course/details/%s",
                    optionalCourse.get().getName().replace(" ", "%20"));
        }

        return "redirect:/";
    }

    @GetMapping("/course/return")
    private String returnToCourseOverview() {
        return "redirect:/course/all";
    }

    @GetMapping("/course/return/{courseId}")
    private String returnToCourseDetails(@PathVariable("courseId") Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            return String.format("redirect:/course/details/%s",
                    optionalCourse.get().getName().replace(" ", "%20"));
        }

        return "redirect:/";
    }
}
