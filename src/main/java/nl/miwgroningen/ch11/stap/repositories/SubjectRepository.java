package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 14:05 on 12 Jun 2023
 * Purpose:
 */
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
