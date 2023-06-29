package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.StudentExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 09:50 on 19 Jun 2023
 * Purpose:
 */
public interface StudentExamAnswerRepository extends JpaRepository<StudentExamAnswer, Long> {
}
