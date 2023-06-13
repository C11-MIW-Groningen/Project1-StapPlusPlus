package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.Student;
import nl.miwgroningen.ch11.stap.model.Subject;
import nl.miwgroningen.ch11.stap.model.Teacher;
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

        deleteDatabase();

        Subject subject1 = Subject.builder()
                .title("Programming")
                .duration(8)
                .build();
        subjectRepository.save(subject1);

        Subject subject2 = Subject.builder()
                .title("Databases")
                .duration(8)
                .build();
        subjectRepository.save(subject2);

        Subject subject3 = Subject.builder()
                .title("Object Oriented Programming")
                .duration(10)
                .build();
        subjectRepository.save(subject3);

        Teacher teacher1 = Teacher.builder()
                .firstName("Vincent")
                .infixName("")
                .lastName("Velthuizen")
                .build();
        teacherRepository.save(teacher1);

        Teacher teacher2 = Teacher.builder()
                .firstName("Martijn")
                .infixName("")
                .lastName("Hiemstra")
                .build();
        teacherRepository.save(teacher2);

        Student student1 = Student.builder()
                .firstName("Tristan")
                .infixName("")
                .lastName("Meinsma")
                .privateEmail("tristan.meinsma@gmail.com")
                .schoolEmail("t.m@hanze.nl")
                .build();
        studentRepository.save(student1);

        Student student2 = Student.builder()
                .firstName("Thijs")
                .infixName("")
                .lastName("Harleman")
                .privateEmail("thijs.harleman@gmail.com")
                .schoolEmail("t.h@hanze.nl")
                .build();
        studentRepository.save(student2);


        return "redirect:/subject/all";
    }

    private void deleteDatabase () {
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
    }
}
