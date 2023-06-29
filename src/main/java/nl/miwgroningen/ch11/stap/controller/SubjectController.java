package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.*;
import nl.miwgroningen.ch11.stap.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Tristan Meinsma
 * This handles interaction with webpage about subjects
 */

@Controller
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {
    private static final String VIEW_SUBJECT_OVERVIEW = "subject/subjectOverview";
    private static final String VIEW_SUBJECT_FORM = "subject/subjectForm";
    private static final String REDIRECT_SUBJECT_OVERVIEW = "redirect:/subject/all";

    private final CourseRepository courseRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamRepository examRepository;
    private final LearningGoalRepository learningGoalRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamAnswerRepository studentExamAnswerRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/all")
    public String showSubjectOverview(Model model) {
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return VIEW_SUBJECT_OVERVIEW;
    }

    @GetMapping("/new")
    public String showSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("allLearningGoals", learningGoalRepository.findAll());
        model.addAttribute("allTeachers", getAllTeachersSorted());

        return VIEW_SUBJECT_FORM;
    }

    @GetMapping("/edit/{subjectId}")
    public String showEditSubjectForm(@PathVariable("subjectId") Long subjectId, Model model) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            model.addAttribute("subject", optionalSubject.get());
            model.addAttribute("allLearningGoals", learningGoalRepository.findAll());
            model.addAttribute("allTeachers", getAllTeachersSorted());

            return VIEW_SUBJECT_FORM;
        }

        return REDIRECT_SUBJECT_OVERVIEW;
    }

    @PostMapping("/new")
    public String saveSubject(@ModelAttribute("subject") Subject subjectToSave, BindingResult result) {
        if (!result.hasErrors()) {
            subjectRepository.save(subjectToSave);
        }

        return REDIRECT_SUBJECT_OVERVIEW;
    }

    @GetMapping("/delete/{subjectId}")
    public String deleteSubject(@PathVariable("subjectId") Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            detachCourses(optionalSubject.get());
            deleteExams(optionalSubject.get());

            subjectRepository.deleteById(subjectId);
        }

        return REDIRECT_SUBJECT_OVERVIEW;
    }

    public void deleteExams(Subject subject) {
        for (Exam exam : subject.getExams()) {
            for (StudentExam studentExam : exam.getStudentExams()) {
                studentExamAnswerRepository.deleteAll(studentExam.getStudentExamAnswers());
                studentExamRepository.delete(studentExam);
            }
            examQuestionRepository.deleteAll(exam.getExamQuestions());
            examRepository.delete(exam);
        }
    }

    public void detachCourses(Subject subject) {
        for (Course course : subject.getCourses()) {
            course.removeSubject(subject);
            courseRepository.save(course);
        }
    }

    public List<Teacher> getAllTeachersSorted() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        Collections.sort(allTeachers);

        return allTeachers;
    }
}

