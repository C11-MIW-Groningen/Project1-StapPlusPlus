package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.repositories.StudentRepository;
import nl.miwgroningen.ch11.stap.repositories.SubjectRepository;
import nl.miwgroningen.ch11.stap.repositories.TeacherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Tristan Meinsma
 * Add initial data to the database
 */
@Controller
@RequiredArgsConstructor
public class SeedController {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/seed")
    private String seedDatabase() {

    }
}
