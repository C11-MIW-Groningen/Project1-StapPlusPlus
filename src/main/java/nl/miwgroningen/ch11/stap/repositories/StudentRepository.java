package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 14:04 on 12 Jun 2023
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
}
