package nl.miwgroningen.ch11.stap.dto;

import lombok.*;
import nl.miwgroningen.ch11.stap.model.Cohort;
import nl.miwgroningen.ch11.stap.model.Student;

/**
 * @author Tristan Meinsma
 * This program helps data transfer between website and model
 */
@Data @Builder
public class EnrollmentDTO {
    private Cohort cohort;
    private Student student;
}
