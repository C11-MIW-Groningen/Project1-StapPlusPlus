package nl.miwgroningen.ch11.stap.configuration;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.service.WebsiteUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Author: Thijs Harleman
 * Created at 14:25 on 13 Jun 2023
 * Purpose: Determine which parts of the site should be secured and how
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final WebsiteUserDetailsService websiteUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/css/**", "/webjars/**").permitAll()
                        .antMatchers("/images/*").permitAll()
                        .antMatchers("/").permitAll()
                        .antMatchers("/course/details/**").permitAll()
                        .antMatchers("/seed").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin().and()
                .logout().logoutSuccessUrl("/");

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(websiteUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }
}

