package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 14:06 on 12 Jun 2023
 * Purpose:
 */
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
