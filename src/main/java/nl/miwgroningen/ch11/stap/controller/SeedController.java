package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import nl.miwgroningen.ch11.stap.model.*;
import nl.miwgroningen.ch11.stap.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Tristan Meinsma
 * Add initial data to the database
 */
@Controller
@RequiredArgsConstructor
public class SeedController {
    private static final int MAXIMUM_LEARNING_GOALS_PER_SUBJECT = 5;
    private static final int SEED_NUMBER_OF_TEACHERS = 10;
    private static final int SEED_NUMBER_OF_STUDENTS = 30;
    private static final int SEED_NUMBER_OF_LEARNING_GOALS = 10;

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private final CourseRepository courseRepository;
    private final LearningGoalRepository learningGoalRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/seed")
    private String seedDatabase() {
        seedLearningGoals();
        seedSubjects();
        seedCourses();
        seedStudents();
        seedTeachers();

        return "redirect:/subject/all";
    }

    private List<LearningGoal> getRandomLearningGoals() {
        List<LearningGoal> allLearningGoals = learningGoalRepository.findAll();
        List<LearningGoal> randomLearningGoals = new ArrayList<>();

        for (int learningGoal = 0; learningGoal < random.nextInt(MAXIMUM_LEARNING_GOALS_PER_SUBJECT); learningGoal++) {
            int randomLearningGoal = random.nextInt(allLearningGoals.size() - 1);
            randomLearningGoals.add(allLearningGoals.get(randomLearningGoal));
        }

        return randomLearningGoals;
    }

    private List<Subject> getRandomSubjects() {
        List<Subject> allSubjects = subjectRepository.findAll();
        List<Subject> randomSubjects = new ArrayList<>();

        for (int subject = 0; subject < random.nextInt(allSubjects.size()); subject++) {
            int randomSubject = random.nextInt(allSubjects.size() - 1);
            randomSubjects.add(allSubjects.get(randomSubject));
        }

        return randomSubjects;
    }

    private void seedCourses() {
        Course course1 = Course.builder()
                .name("Software Engineering")
                .description(faker.lorem().paragraph(2))
                .imageUrl("/images/Software%20Engineering.jpg")
                .subjects(getRandomSubjects())
                .build();
        courseRepository.save(course1);

        Course course2 = Course.builder()
                .name("Functioneel Beheer")
                .description(faker.lorem().paragraph(2))
                .imageUrl("/images/Fucntioneel%20Beheer.jpeg")
                .subjects(getRandomSubjects())
                .build();
        courseRepository.save(course2);
    }

    private void seedLearningGoals() {
        for (int learningGoal = 0; learningGoal < SEED_NUMBER_OF_LEARNING_GOALS; learningGoal++) {
            LearningGoal newLearningGoal = LearningGoal.builder()
                    .title(faker.examplify(faker.lorem().sentence(2)))
                    .description(faker.lorem().paragraph(4))
                    .build();
            learningGoalRepository.save(newLearningGoal);
        }
    }

    private void seedStudents() {
        boolean infix = true;

        for (int student = 0; student < SEED_NUMBER_OF_STUDENTS; student++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            Student newStudent = Student.builder()
                    .firstName(firstName)
                    .infixName(infix ? "van" : "")
                    .lastName(lastName)
                    .privateEmail(String.format("%s.%s@gmail.com", firstName, lastName))
                    .schoolEmail(String.format("%s.%s@st.hanze.nl", firstName.charAt(0), lastName))
                    .build();
            studentRepository.save(newStudent);
            infix = !infix;
        }
    }

    private void seedSubjects() {
        Subject subject1 = Subject.builder()
                .title("Programming")
                .duration(8)
                .learningGoals(getRandomLearningGoals())
                .build();
        subjectRepository.save(subject1);

        Subject subject2 = Subject.builder()
                .title("Databases")
                .duration(8)
                .learningGoals(getRandomLearningGoals())
                .build();
        subjectRepository.save(subject2);

        Subject subject3 = Subject.builder()
                .title("Object Oriented Programming")
                .duration(10)
                .learningGoals(getRandomLearningGoals())
                .build();
        subjectRepository.save(subject3);
    }

    private void seedTeachers() {
        boolean infix = true;

        for (int teacher = 0; teacher < SEED_NUMBER_OF_TEACHERS; teacher++) {
            Teacher newTeacher = Teacher.builder()
                    .firstName(faker.name().firstName())
                    .infixName(infix ? "van" : "")
                    .lastName(faker.name().lastName())
                    .build();
            teacherRepository.save(newTeacher);
            infix = !infix;
        }
    }
}
