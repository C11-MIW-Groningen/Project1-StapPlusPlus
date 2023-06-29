package nl.miwgroningen.ch11.stap.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.LearningGoal;
import nl.miwgroningen.ch11.stap.model.StudentExam;
import nl.miwgroningen.ch11.stap.model.StudentExamQuestion;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: Thijs Harleman
 * Created at 15:27 on 28 Jun 2023
 * Purpose:
 */

@RequiredArgsConstructor
public class PDFExporter {
    private final StudentExam studentExam;

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);

            Paragraph title = new Paragraph(String.format("%s, %s",
                    studentExam.getExam().getDisplayExamTitle(), studentExam.getExam().getDisplayExamSubtitle()), font);

            document.add(title);
            document.add(getTopTable());
            document.add(getExamQuestionsTable());
        } catch (DocumentException documentException) {
            System.err.println(documentException.getMessage());
        }
    }

    private String getLearningGoalsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (LearningGoal learningGoal : studentExam.getExam().getSubject().getLearningGoals()) {
            stringBuilder.append(learningGoal.getTitle()).append(" - ").append(learningGoal.getDescription())
                    .append("\n");
        }

        return stringBuilder.toString();
    }

    private String[] getTopTableValues() {
        return new String[] {
                studentExam.getStudent().getDisplayName(),
                studentExam.getStudent().getSchoolEmail(),
                studentExam.getDisplayGrade(),
                String.format("%s / %s",
                        studentExam.getPointsAttained(), studentExam.getExam().getTotalAttainablePoints()),
                studentExam.getExam().getSubject().getTeacher().getDisplayName(),
                studentExam.getExam().getCohort().getName(),
                studentExam.getExam().getSubject().getTitle(),
                getLearningGoalsString()};
    }

    private PdfPTable getExamQuestionsTable() {
        String[] columnNames = {"Vraag", "Punten", "Feedback"};

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 1.5f, 9f});
        table.setSpacingBefore(10);

        for (String columnName : columnNames) {
            table.addCell(columnName);
        }

        for (StudentExamQuestion studentExamQuestion : studentExam.getStudentExamQuestions()) {
            table.addCell(String.format("Vraag %d", studentExamQuestion.getQuestionNumber()));
            table.addCell(String.format("%d / %d", studentExamQuestion.getPointsAttained(),
                    studentExamQuestion.getAttainablePoints()));
            table.addCell(studentExamQuestion.getFeedback());
        }

        return table;
    }

    private PdfPTable getTopTable() {
        String[] rowNames = {"Student:", "Email:", "Resultaat", "Punten behaald:", "Docent:", "Cohort:", "Vak:",
                "Leerdoelen:"};
        String[] rowValues = getTopTableValues();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.0f, 10.0f});
        table.setSpacingBefore(10);

        for (int row = 0; row < rowNames.length; row++) {
            table.addCell(rowNames[row]);
            table.addCell(rowValues[row]);
        }

        return table;
    }
}
