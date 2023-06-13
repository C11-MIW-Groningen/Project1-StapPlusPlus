package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.WebsiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: Thijs Harleman
 * Created at 14:29 on 13 Jun 2023
 * Purpose:
 */

public interface WebsiteUserRepository extends JpaRepository<WebsiteUser, Long> {
    Optional<WebsiteUser> findByUsername(String username);
}
