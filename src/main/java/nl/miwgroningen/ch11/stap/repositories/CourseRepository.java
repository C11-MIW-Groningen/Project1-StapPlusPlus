package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Tristan Meinsma
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}
