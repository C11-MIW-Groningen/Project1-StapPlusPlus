package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tristan Meinsma
 */
public interface CohortRepository extends JpaRepository<Cohort, Long> {
}
