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
    private static final int COHORT_AMOUNT = 3;
    private static final int EXAM_AMOUNT = 10;
    private static final int EXAM_QUESTION_AMOUNT = 6;
    private static final int STUDENT_AMOUNT = 40;
    private static final int TEACHER_AMOUNT = 10;

    private static final int EXAM_MIN_GRADE = 1;
    private static final int EXAM_MAX_GRADE = 10;
    private static final int EXAM_QUESTION_POINTS = 5;
    private static final int FAKER_SENTENCE_COUNT = 2;
    private static final int GOALS_MAX_PER_SUBJECT = 5;
    private static final int STUDENTS_MAX_PER_COHORT = 25;
    private static final int STUDENTS_MIN_PER_COHORT = 5;

    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final LocalDate START_DATE_FIRST_COHORT = LocalDate.of(2000, 9, 1);

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

        for (int learningGoal = 0; learningGoal < random.nextInt(GOALS_MAX_PER_SUBJECT); learningGoal++) {
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
        String[] names = {"Software Engineering", "Functioneel Beheer"};
        String[] descriptions = {"In de omscholing Software Engineering leer je softwaresystemen ontwerpen, " +
                "realiseren en testen. Je werkt met verschillende programmeertalen en ontwikkelmethoden.",
                faker.lorem().paragraph(FAKER_SENTENCE_COUNT)};
        String[] urls = {"https://www.dqsglobal.com/var/site/storage/images/_aliases/cw_large_" +
                "2x/3/3/4/7/747433-10-ger-DE/f4cb829ab49c-datensicherheit-explore-dqs-shutterstock_1233182206.jpg",
                "https://www.inspry.com/wp-content/uploads/" +
                        "joomla-4-upgrade-vs-wordpress-migration-1-scaled-1-2048x1366.jpg"};

        for (int course = 0; course < names.length; course++) {
            Course newCourse = Course.builder()
                    .name(names[course])
                    .description(descriptions[course])
                    .imageUrl(urls[course])
                    .subjects(subjectRepository.findAll())
                    .build();

            courseRepository.save(newCourse);
        }
    }

    private void seedExams() {
        boolean resit = false;

        for (int exam = 0; exam < EXAM_AMOUNT; exam++) {
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
        for (int examQuestion = 0; examQuestion < EXAM_QUESTION_AMOUNT; examQuestion++) {
            ExamQuestion newExamQuestion = ExamQuestion.builder()
                    .questionNumber(examQuestion + 1)
                    .attainablePoints(EXAM_QUESTION_POINTS)
                    .questionText(faker.lorem().sentence())
                    .exam(examToAddTo)
                    .build();

            examQuestionRepository.save(newExamQuestion);
            examRepository.save(examToAddTo);
        }
    }

    private void seedLearningGoals() {
        String[] titles = {"Model", "View", "Controller", "Testen", "Git / GitHub"};
        String[] descriptions = {"Je hebt van een model class de annotations, " +
                "validation,kardinaliteit en hulp-functies geschreven",
                "Je kunt nieuwe elementen vinden en toepassen om je product beter te maken.",
                "Je hebt in een controller Get en Post mappings geschreven," +
                        "kunt elementen toevoegen en ophalen uit Model en Path",
                "Je hebt een test waarin je expliciet hebt nagedacht over randgevallen (edge cases).",
                "Het lukt je om zelfstandig te werken in Git, je maakt gebruik van de log " +
                        "en kunt zelf merge conflicts oplossen."
        };

        for (int learningGoal = 0; learningGoal < titles.length; learningGoal++) {
            LearningGoal goal = LearningGoal.builder()
                    .title(titles[learningGoal])
                    .description(descriptions[learningGoal])
                    .build();
            learningGoalRepository.save(goal);
        }
    }

    private void seedStudents() {
        boolean infix = true;

        for (int student = 0; student < STUDENT_AMOUNT; student++) {
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

            for (int question = 0; question < EXAM_QUESTION_AMOUNT; question++) {
                int points = Math.max(random.nextInt(EXAM_QUESTION_POINTS + 1),
                        random.nextInt(EXAM_QUESTION_POINTS + 1));
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
                    .description(faker.lorem().paragraph(FAKER_SENTENCE_COUNT))
                    .learningGoals(getRandomLearningGoals())
                    .teacher((getRandomTeacher()))
                    .build();

            subjectRepository.save(newSubject);
        }
    }

    private void seedTeachers() {
        boolean infix = true;

        for (int teacher = 0; teacher < TEACHER_AMOUNT; teacher++) {
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

        for (int cohort = 0; cohort < COHORT_AMOUNT; cohort++) {
            Cohort newCohort = Cohort.builder()
                    .name(String.valueOf(cohort + 1))
                    .startDate(START_DATE_FIRST_COHORT.plusYears(cohort))
                    .course(getRandomCourse())
                    .students(getRandomStudents(numberOfStudentsPerCohort.get(cohort)))
                    .build();
            cohortRepository.save(newCohort);
        }
    }

    private List<Integer> getNumberOfStudentsPerCohort() {
        List<Integer> numberOfStudentsPerCohort = new ArrayList<>();
        numberOfStudentsPerCohort.add(STUDENTS_MIN_PER_COHORT);
        numberOfStudentsPerCohort.add(STUDENTS_MAX_PER_COHORT);

        for (int i = 2; i < COHORT_AMOUNT; i++) {
            numberOfStudentsPerCohort.add(
                    random.nextInt(STUDENTS_MAX_PER_COHORT - STUDENTS_MIN_PER_COHORT) + STUDENTS_MIN_PER_COHORT);
        }

        return numberOfStudentsPerCohort;
    }

    private void updateGrades(StudentExam studentExam, int points) {
        studentExam.setPointsAttained(points);
        studentExam.setGrade( EXAM_MIN_GRADE + (EXAM_MAX_GRADE - EXAM_MIN_GRADE) *
                (double) points / (EXAM_QUESTION_POINTS * EXAM_QUESTION_AMOUNT));

        studentExamRepository.save(studentExam);
    }
}
