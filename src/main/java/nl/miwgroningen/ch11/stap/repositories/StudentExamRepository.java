package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 09:49 on 19 Jun 2023
 * Purpose:
 */
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
}
