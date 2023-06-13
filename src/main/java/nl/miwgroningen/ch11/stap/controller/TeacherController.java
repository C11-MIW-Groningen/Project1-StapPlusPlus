package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Teacher;
import nl.miwgroningen.ch11.stap.repositories.TeacherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: Thijs Harleman
 * Created at 14:02 on 12 Jun 2023
 * Purpose: Handle interactions with webpages about teachers
 */

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;

    @GetMapping( "/all")
    private String showTeacherOverview(Model model) {
        model.addAttribute("allTeachers", teacherRepository.findAll());
        model.addAttribute("newTeacher", new Teacher());
        return "teacherOverview";
    }

    @GetMapping("/new")
    private String showTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());

        return "teacherForm";
    }

    @PostMapping("/new")
    private String saveOrUpdateTeacher(@ModelAttribute("newTeacher") Teacher teacher, BindingResult result) {
        if (!result.hasErrors()) {
            teacherRepository.save(teacher);
        }

        return "redirect:/teacher/all";
    }
}
