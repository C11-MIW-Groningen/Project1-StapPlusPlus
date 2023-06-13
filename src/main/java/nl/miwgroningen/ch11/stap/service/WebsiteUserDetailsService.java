package nl.miwgroningen.ch11.stap.service;

import lombok.RequiredArgsConstructor;
import nl.miwgroningen.ch11.stap.repositories.WebsiteUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Author: Thijs Harleman
 * Created at 14:27 on 13 Jun 2023
 * Purpose: Provide details for a user
 */

@Service
@RequiredArgsConstructor
public class WebsiteUserDetailsService implements UserDetailsService {
    private final WebsiteUserRepository websiteUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return websiteUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }
}
