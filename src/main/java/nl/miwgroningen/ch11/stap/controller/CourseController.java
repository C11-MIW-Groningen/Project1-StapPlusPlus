package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Course;
import nl.miwgroningen.ch11.stap.model.Subject;
import nl.miwgroningen.ch11.stap.repositories.CourseRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private String showSubjectOverview(Model model) {
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

    @GetMapping("/course/remove/{courseId}/{subjectId}")
    private String removeCourse(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalCourse.isPresent() && optionalSubject.isPresent()) {
            List<Subject> courseSubjects = optionalCourse.get().getSubjects();
            courseSubjects.remove(optionalSubject.get());
            optionalCourse.get().setSubjects(courseSubjects);
            courseRepository.save(optionalCourse.get());
        }

        return "redirect:/";
    }
}
