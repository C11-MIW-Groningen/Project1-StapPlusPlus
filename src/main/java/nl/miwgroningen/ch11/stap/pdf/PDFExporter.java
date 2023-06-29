package nl.miwgroningen.ch11.stap.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.LearningGoal;
import nl.miwgroningen.ch11.stap.model.StudentExam;
import nl.miwgroningen.ch11.stap.model.StudentExamAnswer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: Thijs Harleman
 * Created at 15:27 on 28 Jun 2023
 * Purpose:
 */

@RequiredArgsConstructor
public class PDFExporter {
    private static final float[] EXAM_INFO_COLUMN_WIDTHS = {3.0f, 9.0f};
    private static final float[] QUESTIONS_COLUMN_WIDTHS = {1.5f, 1.5f, 9.0f};
    private static final int TABLE_SPACING = 10;
    private static final float TABLE_WIDTH_PERCENTAGE = 100f;
    private static final int TITLE_FONT_SIZE = 18;

    private final StudentExam studentExam;

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(TITLE_FONT_SIZE);

            Paragraph title = new Paragraph(String.format("%s, %s",
                    studentExam.getExam().getDisplayExamTitle(), studentExam.getExam().getDisplayExamSubtitle()), font);

            document.add(title);
            document.add(getExamInfoTable());
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

    private String[] getExamInfoTableValues() {
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

    private PdfPTable buildNewTable(float[] columnWidths) {
        PdfPTable table = new PdfPTable(columnWidths.length);
        table.setWidthPercentage(TABLE_WIDTH_PERCENTAGE);
        table.setWidths(columnWidths);
        table.setSpacingBefore(TABLE_SPACING);

        return table;
    }

    private PdfPTable getExamQuestionsTable() {
        String[] columnNames = {"Vraag", "Punten", "Feedback"};

        PdfPTable table = buildNewTable(QUESTIONS_COLUMN_WIDTHS);

        for (String columnName : columnNames) {
            table.addCell(columnName);
        }

        for (StudentExamAnswer studentExamAnswer : studentExam.getStudentExamAnswers()) {
            table.addCell(String.format("Vraag %d", studentExamAnswer.getQuestionNumber()));
            table.addCell(String.format("%d / %d", studentExamAnswer.getPointsAttained(),
                    studentExamAnswer.getAttainablePoints()));
            table.addCell(studentExamAnswer.getFeedback());
        }

        return table;
    }

    private PdfPTable getExamInfoTable() {
        String[] rowNames = {"Student:", "Email:", "Resultaat", "Punten behaald:", "Docent:", "Cohort:", "Vak:",
                "Leerdoelen:"};
        String[] rowValues = getExamInfoTableValues();

        PdfPTable table = buildNewTable(EXAM_INFO_COLUMN_WIDTHS);

        for (int row = 0; row < rowNames.length; row++) {
            table.addCell(rowNames[row]);
            table.addCell(rowValues[row]);
        }

        return table;
    }
}
