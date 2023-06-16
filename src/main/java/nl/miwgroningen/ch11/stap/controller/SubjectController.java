package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Subject;
import nl.miwgroningen.ch11.stap.model.Teacher;
import nl.miwgroningen.ch11.stap.repositories.LearningGoalRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
import nl.miwgroningen.ch11.stap.repositories.TeacherRepository;
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
    private final LearningGoalRepository learningGoalRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/all")
    private String showSubjectOverview(Model model) {
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return "subjectOverview";
    }

    @GetMapping("/new")
    private String showSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("allLearningGoals", learningGoalRepository.findAll());
        model.addAttribute("allTeachers", getAllTeachersSorted());

        return "subjectForm";
    }

    @GetMapping("/edit/{subjectId}")
    private String showEditSubjectForm(@PathVariable("subjectId") Long subjectId, Model model) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            model.addAttribute("subject", optionalSubject.get());
            model.addAttribute("allLearningGoals", learningGoalRepository.findAll());
            model.addAttribute("allTeachers", getAllTeachersSorted());

            return "subjectForm";
        }

        return "redirect:/";
    }

    @PostMapping("/new")
    private String saveSubject(@ModelAttribute("subject") Subject subjectToSave, BindingResult result) {
        if (!result.hasErrors()) {
            subjectRepository.save(subjectToSave);
        }

        return "redirect:/";
    }

    @GetMapping("/delete/{subjectId}")
    private String deleteSubject(@PathVariable("subjectId") Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        optionalSubject.ifPresent(subjectRepository::delete);

        return "redirect:/";
    }

    private List<Teacher> getAllTeachersSorted() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        Collections.sort(allTeachers);

        return allTeachers;
    }
}

