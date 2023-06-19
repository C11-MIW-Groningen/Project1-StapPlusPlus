package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 09:48 on 19 Jun 2023
 * Purpose:
 */
public interface ExamRepository extends JpaRepository<Exam, Long> {
}
