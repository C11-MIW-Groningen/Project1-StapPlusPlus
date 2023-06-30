package nl.miwgroningen.ch11.stap;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.model.WebsiteUser;
import nl.miwgroningen.ch11.stap.repositories.WebsiteUserRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.logging.log4j.Logger;

/**
 * Author: Thijs Harleman
 * Created at 14:31 on 13 Jun 2023
 * Purpose:
 */

@SpringBootApplication
@RequiredArgsConstructor
public class ApplicationKickstarter implements CommandLineRunner {
    private static final String DEFAULT_USERNAME_ADMIN = "admin";
    private static final String DEFAULT_PASSWORD_ADMIN = "changethis";

    private static final Logger logger = LogManager.getLogger();

    private final WebsiteUserRepository websiteUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (websiteUserRepository.findByUsername(DEFAULT_USERNAME_ADMIN).isEmpty()) {
            WebsiteUser admin = new WebsiteUser();
            admin.setUsername(DEFAULT_USERNAME_ADMIN);
            admin.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD_ADMIN));
            admin.setAdministrator(true);

            websiteUserRepository.save(admin);
            logger.warn("Admin created. Remember to change the password!");
        }
    }
}
