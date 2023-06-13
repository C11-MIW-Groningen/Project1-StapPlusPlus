package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tristan Meinsma
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

}
