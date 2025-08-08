package org.pizzeria.dashboard.util;

import org.pizzeria.dashboard.model.User;
import org.pizzeria.dashboard.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByEmail("admin@pizzeria.it").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@pizzeria.it");
                admin.setPassword(encoder.encode("admin"));
                admin.setRole("MANAGER");
                admin.setActive(true);
                userRepo.save(admin);
            }
        };
    }
}
