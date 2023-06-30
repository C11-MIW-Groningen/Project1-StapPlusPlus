package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tristan Meinsma
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

}
