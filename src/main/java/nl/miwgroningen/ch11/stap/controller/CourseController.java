package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Course;
import nl.miwgroningen.ch11.stap.model.Image;
import nl.miwgroningen.ch11.stap.model.Subject;
import nl.miwgroningen.ch11.stap.repositories.CohortRepository;
import nl.miwgroningen.ch11.stap.repositories.CourseRepository;
import nl.miwgroningen.ch11.stap.repositories.ImageRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * @author Tristan Meinsma
 * This handles interaction on webpage about courses
 */

@Controller
@RequiredArgsConstructor
public class CourseController {
    private static final String REDIRECT_COURSE_DETAILS = "redirect:/course/details/%s";
    private static final String REDIRECT_COURSE_OVERVIEW = "redirect:/course/all";
    private static final String REDIRECT_ROOT = "redirect:/";
    private static final String VIEW_COURSE_OVERVIEW = "course/courseOverview";
    private static final String VIEW_COURSE_DETAILS = "course/courseDetails";
    private static final String VIEW_COURSE_FORM = "course/courseForm";
    private static final String VIEW_LANDING_PAGE = "general/landingPage";

    private final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    private final CohortRepository cohortRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final ImageRepository imageRepository;

    @GetMapping("/")
    public String showLandingPage(Model model) {
        model.addAttribute("allCourses", courseRepository.findAll());

        return VIEW_LANDING_PAGE;
    }

    @GetMapping("/course/all")
    public String showCourseOverview(Model model) {
        model.addAttribute("allCourses", courseRepository.findAll());

        return VIEW_COURSE_OVERVIEW;
    }

    @GetMapping("/course/details/{name}")
    public String showCourseDetails(@PathVariable("name") String name, Model model) {
        Optional<Course> optionalCourse = courseRepository.findByName(name);

        if (optionalCourse.isPresent()) {
            model.addAttribute("shownCourse", optionalCourse.get());

            return VIEW_COURSE_DETAILS;
        }

        return REDIRECT_ROOT;
    }

    @GetMapping("/course/new")
    public String showCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return VIEW_COURSE_FORM;
    }

    @GetMapping("/course/edit/{courseId}")
    public String showEditCourseForm(@PathVariable("courseId") Long courseId, Model model) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            model.addAttribute("course", optionalCourse.get());
            model.addAttribute("allSubjects", subjectRepository.findAll());

            return VIEW_COURSE_FORM;
        }

        return REDIRECT_ROOT;
    }

    @PostMapping("/course/new")
    public String saveCourse(@ModelAttribute("course") Course courseToSave,
                             @RequestParam("imageFile") MultipartFile file,
                             BindingResult result) {

        if (!result.hasErrors()) {
            if (!file.isEmpty()) {
                try {
                    Image image = saveImage(file);
                    courseToSave.setImage(image);
                } catch (IOException exception) {
                    System.err.println(exception.getMessage());
                }
            }
            courseRepository.save(courseToSave);
        }

        return String.format(REDIRECT_COURSE_DETAILS, courseToSave.getName().replace(" ", "%20"));
    }



    private Image saveImage(MultipartFile file) throws IOException {

        StringBuilder fileName = new StringBuilder();
        fileName.append(UUID.randomUUID());
        if (getExtensionByStringHandling(file.getOriginalFilename()).isPresent()) {
            fileName.append(".").append(getExtensionByStringHandling(file.getOriginalFilename()).get());
        }
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName.toString());
        Files.write(fileNameAndPath, file.getBytes());

        Image image = new Image();
        image.setImageName(fileName.toString());
        return imageRepository.save(image);
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

        public byte[] getBase64BytesEncoding(MultipartFile file) {
        try {
            byte[] fileContent = file.getBytes();
            return Base64.getEncoder().encode(fileContent);
        } catch (IOException e) {
            // Handle the exception according to your application's requirements
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/course/delete/{courseId}")
    public String deleteCourse(@PathVariable("courseId") Long courseId) {
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

        return REDIRECT_COURSE_OVERVIEW;
    }

    @GetMapping("/course/remove/{courseId}/{subjectId}")
    public String removeSubject(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalCourse.isPresent()) {
            if (optionalSubject.isPresent()) {
                List<Subject> courseSubjects = optionalCourse.get().getSubjects();
                courseSubjects.remove(optionalSubject.get());
                optionalCourse.get().setSubjects(courseSubjects);
                courseRepository.save(optionalCourse.get());
            }
            return String.format(REDIRECT_COURSE_DETAILS,
                    optionalCourse.get().getName().replace(" ", "%20"));
        }

        return REDIRECT_ROOT;
    }

    @GetMapping("/course/return")
    public String returnToCourseOverview() {
        return REDIRECT_COURSE_OVERVIEW;
    }

    @GetMapping("/course/return/{courseId}")
    public String returnToCourseDetails(@PathVariable("courseId") Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            return String.format(REDIRECT_COURSE_DETAILS,
                    optionalCourse.get().getName().replace(" ", "%20"));
        }

        return REDIRECT_ROOT;
    }
}
