package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Course;
import nl.miwgroningen.ch11.stap.model.Subject;
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
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/")
    private String showCourseOverview(Model model) {
        model.addAttribute("allCourses", courseRepository.findAll());
        return "courseOverview";
    }

    @GetMapping("/course/details/{name}")
    private String showCourseDetails(@PathVariable("name") String name, Model model) {
        Optional<Course> optionalCourse = courseRepository.findByName(name);

        if (optionalCourse.isPresent()) {
            model.addAttribute("shownCourse", optionalCourse.get());

            return "courseDetails";
        }

        return "redirect:/";
    }

    @GetMapping("/course/new")
    private String showCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return "courseForm";
    }

    @GetMapping("/course/edit/{courseId}")
    private String showEditCourseForm(@PathVariable("courseId") Long courseId, Model model) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            model.addAttribute("course", optionalCourse.get());
            model.addAttribute("allSubjects", subjectRepository.findAll());

            return "courseForm";
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
