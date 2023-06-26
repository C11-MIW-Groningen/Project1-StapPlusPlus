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
    private static final int SEED_NUMBER_OF_COHORTS = 3;
    private static final int SEED_NUMBER_OF_EXAMS = 10;
    private static final int SEED_NUMBER_OF_EXAM_QUESTIONS_PER_EXAM = 6;
    private static final int SEED_NUMBER_OF_STUDENTS = 40;
    private static final int SEED_NUMBER_OF_TEACHERS = 10;

    private static final int MIN_NUMBER_OF_STUDENTS_PER_COHORT = 5;
    private static final int MAX_NUMBER_OF_STUDENTS_PER_COHORT = 25;
    private static final int SEED_EXAM_QUESTION_ATTAINABLE_POINTS = 5;

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private final CohortRepository cohortRepository;
    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final LearningGoalRepository learningGoalRepository;
    private final StudentRepository studentRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamQuestionRepository studentExamQuestionRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final WebsiteUserRepository websiteUserRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/seed")
    public String seedDatabase() {
        seedLearningGoals();
        seedTeachers();
        seedSubjects();
        seedCourses();
        seedStudents();
        seedWebsiteUsers();
        seedCohorts();
        seedExams();
        seedStudentExams();
        seedStudentExamQuestions();

        return "redirect:/";
    }

    private Cohort getRandomCohort() {
        List<Cohort> allCohorts = cohortRepository.findAll();
        int randomCohort = random.nextInt(allCohorts.size() - 1);

        return allCohorts.get(randomCohort);
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

            while (randomStudents.contains(allStudents.get(randomStudent))) {
                randomStudent = random.nextInt(allStudents.size() - 1);
            }

            randomStudents.add(allStudents.get(randomStudent));
        }
        return randomStudents;
    }

    private Subject getRandomSubject() {
        List<Subject> allSubjects = subjectRepository.findAll();
        int randomSubject = random.nextInt(allSubjects.size() - 1);

        return allSubjects.get(randomSubject);
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
                .imageUrl("https://www.dqsglobal.com/var/site/storage/images/_" +
                        "aliases/cw_large_2x/3/3/4/7/747433-10-ger-DE/f4cb829ab49c-" +
                        "datensicherheit-explore-dqs-shutterstock_1233182206.jpg")
                .subjects(subjectRepository.findAll())
                .build();
        courseRepository.save(course1);

        Course course2 = Course.builder()
                .name("Functioneel Beheer")
                .description(faker.lorem().paragraph(3))
                .imageUrl("https://www.inspry.com/wp-content/uploads/joomla-4-upgrade-vs-" +
                        "wordpress-migration-1-scaled-1-2048x1366.jpg")
                .subjects(subjectRepository.findAll())
                .build();
        courseRepository.save(course2);
    }

    private void seedExams() {
        boolean resit = false;

        for (int exam = 0; exam < SEED_NUMBER_OF_EXAMS; exam++) {

            Cohort randomCohort = getRandomCohort();

            Exam newExam = Exam.builder()
                    .cohort(randomCohort)
                    .examDate(randomCohort.getStartDate().plusWeeks(exam + 1L))
                    .subject(getRandomSubject())
                    .resit(resit)
                    .build();
            examRepository.save(newExam);
            seedExamQuestions(newExam);
            resit = !resit;
        }
    }

    private void seedExamQuestions(Exam examToAddTo) {
        for (int examQuestion = 0; examQuestion < SEED_NUMBER_OF_EXAM_QUESTIONS_PER_EXAM; examQuestion++) {
            ExamQuestion newExamQuestion = ExamQuestion.builder()
                    .questionNumber(examQuestion + 1)
                    .attainablePoints(SEED_EXAM_QUESTION_ATTAINABLE_POINTS)
                    .questionText(faker.lorem().sentence())
                    .exam(examToAddTo)
                    .build();
            examQuestionRepository.save(newExamQuestion);
            examRepository.save(examToAddTo);
        }
    }

    private void seedLearningGoals() {
        LearningGoal model = LearningGoal.builder()
                .title("Model")
                .description("Je hebt van een model class de annotations, " +
                        "validation,kardinaliteit en hulp-functies geschreven")
                .build();
        learningGoalRepository.save(model);

        LearningGoal view = LearningGoal.builder()
                .title("View")
                .description("Je kunt nieuwe elementen vinden en toepassen om je product beter te maken.")
                .build();
        learningGoalRepository.save(view);

        LearningGoal controller = LearningGoal.builder()
                .title("Controller")
                .description("Je hebt in een controller Get en Post mappings geschreven," +
                        "kunt elementen toevoegen en ophalen uit Model en Path")
                .build();
        learningGoalRepository.save(controller);

        LearningGoal testen = LearningGoal.builder()
                .title("Testen")
                .description("Je hebt een test waarin je expliciet hebt nagedacht over randgevallen (edge cases).")
                .build();
        learningGoalRepository.save(testen);

        LearningGoal git = LearningGoal.builder()
                .title("Git / GitHub")
                .description("Het lukt je om zelfstandig te werken in Git, je maakt gebruik van de log " +
                        "en kunt zelf merge conflicts oplossen.")
                .build();
        learningGoalRepository.save(git);
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

    private void seedStudentExams() {
        for (Exam exam : examRepository.findAll()) {
            for (Student student : exam.getCohort().getStudents()) {
                StudentExam newStudentExam = StudentExam.builder()
                        .exam(exam)
                        .student(student)
                        .build();
                studentExamRepository.save(newStudentExam);
            }
        }
    }

    private void seedStudentExamQuestions() {
        for (StudentExam studentExam : studentExamRepository.findAll()) {
            int totalPoints = 0;

            for (int question = 0; question < SEED_NUMBER_OF_EXAM_QUESTIONS_PER_EXAM; question++) {
                int points = Math.max(random.nextInt(SEED_EXAM_QUESTION_ATTAINABLE_POINTS + 1),
                        random.nextInt(SEED_EXAM_QUESTION_ATTAINABLE_POINTS + 1));
                totalPoints += points;

                StudentExamQuestion studentExamQuestion = StudentExamQuestion.builder()
                        .questionNumber(question + 1)
                        .pointsAttained(points)
                        .feedback(faker.lorem().sentence())
                        .studentExam(studentExam)
                        .build();
                studentExamQuestionRepository.save(studentExamQuestion);
            }

            updateGrades(studentExam, totalPoints);
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

    private void updateGrades(StudentExam studentExam, int points) {
        studentExam.setPointsAttained(points);
        studentExam.setGrade( 1 + 9 * (double) points /
                (SEED_EXAM_QUESTION_ATTAINABLE_POINTS * SEED_NUMBER_OF_EXAM_QUESTIONS_PER_EXAM));
        studentExamRepository.save(studentExam);
    }
}
