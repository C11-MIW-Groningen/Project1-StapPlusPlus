package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Teacher;
import nl.miwgroningen.ch11.stap.repositories.TeacherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        List<Teacher> teachers = teacherRepository.findAll();
        Collections.sort(teachers);
        model.addAttribute("allTeachers", teachers);
        model.addAttribute("newTeacher", new Teacher());
        return "teacherOverview";
    }

    @GetMapping("/new")
    private String showTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());

        return "teacherForm";
    }

    @GetMapping("/edit/{teacherId}")
    private String showEditTeacherForm(@PathVariable("teacherId") Long teacherId, Model model) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isPresent()) {
            model.addAttribute("teacher", optionalTeacher.get());
            model.addAttribute("allTeachers", teacherRepository.findAll());
            return "teacherForm";
        }

        return "redirect:/teacher/all";
    }

    @GetMapping("/delete/{teacherId}")
    private String deleteTeacher(@PathVariable("teacherId") Long teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        optionalTeacher.ifPresent(teacherRepository::delete);

        return "redirect:/teacher/all";
    }

    @PostMapping("/new")
    private String saveOrUpdateTeacher(@ModelAttribute("newTeacher") Teacher teacher, BindingResult result) {
        if (!result.hasErrors()) {
            teacherRepository.save(teacher);
        }

        return "redirect:/teacher/all";
    }
}
