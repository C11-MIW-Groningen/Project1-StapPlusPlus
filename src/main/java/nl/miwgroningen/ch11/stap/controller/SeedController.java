package nl.miwgroningen.ch11.stap.controller;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import nl.miwgroningen.ch11.stap.model.*;
import nl.miwgroningen.ch11.stap.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
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
    private static final int SEED_NUMBER_OF_STUDENTS = 100;
    private static final int SEED_NUMBER_OF_LEARNING_GOALS = 10;
    private static final int SEED_NUMBER_OF_COHORTS = 6;
    private static final int MIN_NUMBER_OF_STUDENTS_PER_COHORT = 5;
    private static final int MAX_NUMBER_OF_STUDENTS_PER_COHORT = 25;

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private final CourseRepository courseRepository;
    private final LearningGoalRepository learningGoalRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final WebsiteUserRepository websiteUserRepository;
    private final CohortRepository cohortRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/seed")
    private String seedDatabase() {
        seedLearningGoals();
        seedTeachers();
        seedSubjects();
        seedCourses();
        seedStudents();
        seedWebsiteUsers();
        seedCohorts();

        return "redirect:/";
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

    private List<Student> getRandomStudents(int numberOfStudents) {
        List<Student> allStudents = studentRepository.findAll();
        List<Student> randomStudents = new ArrayList<>();

            for (int student = 0; student < numberOfStudents; student++) {
                int randomStudent = random.nextInt(allStudents.size() - 1);
                randomStudents.add(allStudents.get(randomStudent));
            }
        return randomStudents;
    }

    private Teacher getRandomTeacher() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        int randomTeacher = random.nextInt(allTeachers.size() - 1);

        return allTeachers.get(randomTeacher);
    }

    private Course getRandomCourse() {
        List<Course> allCourses = courseRepository.findAll();
        int randomCourse = random.nextInt(allCourses.size() - 1);

        return allCourses.get(randomCourse);
    }

    private void seedCourses() {
        Course course1 = Course.builder()
                .name("Software Engineering")
                .description("In de omscholing Software Engineering leer je softwaresystemen ontwerpen, realiseren " +
                        "en testen. Je werkt met verschillende programmeertalen en ontwikkelmethoden.")
                .imageUrl("/images/Software%20Engineering.jpg")
                .subjects(subjectRepository.findAll())
                .build();
        courseRepository.save(course1);

        Course course2 = Course.builder()
                .name("Functioneel Beheer")
                .description(faker.lorem().paragraph(3))
                .imageUrl("/images/Fucntioneel%20Beheer.jpeg")
                .subjects(subjectRepository.findAll())
                .build();
        courseRepository.save(course2);
    }

    private void seedLearningGoals() {
        for (int learningGoal = 0; learningGoal < SEED_NUMBER_OF_LEARNING_GOALS; learningGoal++) {
            LearningGoal newLearningGoal = LearningGoal.builder()
                    .title(faker.lorem().sentence(2))
                    .description(faker.lorem().paragraph(3))
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
        String[] subjects = {"Programming", "Databases", "Object Oriented Programming", "Intermediate Programming",
        "Advanced Programming", "Design Patterns", "Scrum"};
        int[] subjectDurations = {8, 8, 8, 22, 50, 2, 2};

        for (int subject = 0; subject < subjects.length; subject++) {
            Subject newSubject = Subject.builder()
                    .title(subjects[subject])
                    .duration(subjectDurations[subject])
                    .description(faker.lorem().paragraph(2))
                    .learningGoals(getRandomLearningGoals())
                    .teacher((getRandomTeacher()))
                    .build();
            subjectRepository.save(newSubject);
        }
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

    private void seedWebsiteUsers() {
        WebsiteUser websiteUser = WebsiteUser.builder()
                .username("user")
                .password(passwordEncoder.encode("userPW"))
                .administrator(false)
                .build();
        websiteUserRepository.save(websiteUser);
    }

    private void seedCohorts() {
        List<Integer> numberOfStudentsPerCohort = getNumberOfStudentsPerCohort();

        for (int cohort = 0; cohort < SEED_NUMBER_OF_COHORTS; cohort++) {
            Cohort newCohort = Cohort.builder()
                    .number(String.valueOf(cohort + 1))
                    .startDate(LocalDate.of(2000 + cohort, 9, 1))
                    .course(getRandomCourse())
                    .students(getRandomStudents(numberOfStudentsPerCohort.get(cohort)))
                    .build();
            cohortRepository.save(newCohort);
        }
    }

    private List<Integer> getNumberOfStudentsPerCohort() {
        List<Integer> numberOfStudentsPerCohort = new ArrayList<>();
        numberOfStudentsPerCohort.add(MIN_NUMBER_OF_STUDENTS_PER_COHORT);
        numberOfStudentsPerCohort.add(MAX_NUMBER_OF_STUDENTS_PER_COHORT);

        for (int i = 2; i < SEED_NUMBER_OF_COHORTS; i++) {
            numberOfStudentsPerCohort.add(random.nextInt(
                    MAX_NUMBER_OF_STUDENTS_PER_COHORT - MIN_NUMBER_OF_STUDENTS_PER_COHORT)
                    + MIN_NUMBER_OF_STUDENTS_PER_COHORT);
        }

        return numberOfStudentsPerCohort;
    }
}
