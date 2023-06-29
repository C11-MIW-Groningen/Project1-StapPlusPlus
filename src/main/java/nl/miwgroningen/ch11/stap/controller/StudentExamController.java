package nl.miwgroningen.ch11.stap.controller;

import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.*;
import nl.miwgroningen.ch11.stap.pdf.PDFExporter;
import nl.miwgroningen.ch11.stap.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Author: Thijs Harleman
 * Created at 09:46 on 19 Jun 2023
 * Purpose: Handle interactions with student responses for exams.
 */

@Controller
@RequestMapping("/studentExam")
@RequiredArgsConstructor
public class StudentExamController {
    private static final String REDIRECT_EXAM_OVERVIEW = "redirect:/exam/all";
    private static final String REDIRECT_EXAM_RESULTS = "redirect:/exam/results/%d";
    private static final String VIEW_STUDENT_EXAM_FORM = "studentExam/studentExamForm";
    private static final String VIEW_STUDENT_EXAM_DETAILS = "studentExam/studentExamDetails";

    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final StudentExamRepository studentExamRepository;
    private final StudentExamAnswerRepository studentExamAnswerRepository;

    @GetMapping("/new/{examId}")
    public String showStudentExamForm(@PathVariable("examId") Long examId, Model model) {
        Optional<Exam> optionalExam = examRepository.findById(examId);

        if (optionalExam.isPresent()) {
            StudentExam studentExam = new StudentExam();
            studentExam.setExam(optionalExam.get());

            studentExamRepository.save(studentExam);
            addStudentExamAnswers(studentExam);

            model.addAttribute("studentExam", studentExam);
            model.addAttribute("studentsWithoutExam", getStudentsWithoutExam(optionalExam.get()));

            return VIEW_STUDENT_EXAM_FORM;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/edit/{studentExamId}")
    public String showEditStudentExamForm(@PathVariable("studentExamId") Long studentExamId, Model model) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            List<Student> studentsWithoutExam = getStudentsWithoutExam(optionalStudentExam.get().getExam());
            studentsWithoutExam.add(optionalStudentExam.get().getStudent());

            model.addAttribute("studentExam", optionalStudentExam.get());
            model.addAttribute("studentsWithoutExam", studentsWithoutExam);

            return VIEW_STUDENT_EXAM_FORM;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @PostMapping("/new")
    public String saveStudentExam(@ModelAttribute("studentExam") StudentExam studentExamToSave, BindingResult result) {
        if (!result.hasErrors()) {
            studentExamAnswerRepository.saveAll(studentExamToSave.getStudentExamAnswers());
            studentExamToSave.setPointsAttained();
            studentExamToSave.setGrade();
            studentExamRepository.save(studentExamToSave);
        }

        return String.format(REDIRECT_EXAM_RESULTS, studentExamToSave.getExam().getExamId());
    }

    @GetMapping("/delete/{studentExamId}")
    public String deleteExam(@PathVariable("studentExamId") Long studentExamId) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            Exam exam = optionalStudentExam.get().getExam();

            studentExamAnswerRepository.deleteAll(optionalStudentExam.get().getStudentExamAnswers());
            studentExamRepository.delete(optionalStudentExam.get());

            return String.format(REDIRECT_EXAM_RESULTS, exam.getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/details/{studentExamId}")
    public String showStudentExamDetails(@PathVariable("studentExamId") Long studentExamId, Model model) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            model.addAttribute("shownStudentExam", optionalStudentExam.get());

            return VIEW_STUDENT_EXAM_DETAILS;
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/cancel/{studentExamId}")
    public String cancelStudentExam(@PathVariable("studentExamId") Long studentExamId) {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            Exam exam = optionalStudentExam.get().getExam();

            if (optionalStudentExam.get().getStudent() == null) {
                deleteStudentExam(optionalStudentExam.get());
            }

            return String.format(REDIRECT_EXAM_RESULTS, exam.getExamId());
        }

        return REDIRECT_EXAM_OVERVIEW;
    }

    @GetMapping("/export/{studentExamId}")
    public void exportStudentExamDetails(@PathVariable("studentExamId") Long studentExamId,
                                           HttpServletResponse response) throws DocumentException, IOException {
        Optional<StudentExam> optionalStudentExam = studentExamRepository.findById(studentExamId);

        if (optionalStudentExam.isPresent()) {
            response.setContentType("application/pdf");

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=%s_%s.pdf",
                    optionalStudentExam.get().getExam().getSubject().getTitle().replace(" ", "_"),
                    optionalStudentExam.get().getStudent().getLastName());
            response.setHeader(headerKey, headerValue);

            PDFExporter exporter = new PDFExporter(optionalStudentExam.get());
            exporter.export(response);
        }
    }

    private void addStudentExamAnswers(StudentExam studentExam) {
        for (ExamQuestion examQuestion : studentExam.getExam().getExamQuestions()) {
            StudentExamAnswer studentExamAnswer = new StudentExamAnswer();
            studentExamAnswer.setQuestionNumber(examQuestion.getQuestionNumber());
            studentExamAnswer.setStudentExam(studentExam);
            studentExam.getStudentExamAnswers().add(studentExamAnswer);
            studentExamAnswer.setPointsAttained(studentExamAnswer.getAttainablePoints());

            studentExamAnswerRepository.save(studentExamAnswer);
        }
    }

    private void deleteStudentExam(StudentExam studentExam) {
        for (StudentExamAnswer studentExamAnswer : studentExam.getStudentExamAnswers()) {
            studentExamAnswerRepository
                    .deleteAll(studentExamAnswer.getStudentExam().getStudentExamAnswers());
        }

        studentExamRepository.delete(studentExam);
    }

    private List<Student> getStudentsWithoutExam(Exam exam) {
        List<Student> students;
        List<StudentExam> studentExams = exam.getStudentExams();

        if (exam.getCohort() != null) {
            students = exam.getCohort().getStudents();
        } else {
            students = studentRepository.findAll();
        }

        for (StudentExam studentExam : studentExams) {
            students.remove(studentExam.getStudent());
        }

        return students;
    }
}
