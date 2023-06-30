package nl.miwgroningen.ch11.stap.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * @author Tristan Meinsma
 * Handles photo uploads
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDirectory = System.getProperty("user.dir") + "/uploads/";

        // Create the upload directory if it doesn't exist
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create the upload directory");
            }
        }

        // Map the "/photos" URL path to the upload directory
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:" + uploadDirectory);
    }
}
