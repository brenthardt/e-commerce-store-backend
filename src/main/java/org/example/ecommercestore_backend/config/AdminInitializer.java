package org.example.ecommercestore_backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.entity.User;
import org.example.ecommercestore_backend.repository.RoleRepository;
import org.example.ecommercestore_backend.repository.UserRepository;
import org.example.ecommercestore_backend.role.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        String adminEmail = "admin1111@gmail.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setName("Admin");
            superAdmin.setEmail(adminEmail);
            superAdmin.setPassword(passwordEncoder.encode("1056"));
            superAdmin.setRoles(List.of(adminRole));

            userRepository.save(superAdmin);
        }

    }
}

