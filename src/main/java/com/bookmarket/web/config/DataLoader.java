package com.bookmarket.web.config;

import com.bookmarket.web.entity.User;
import com.bookmarket.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@bookmarket.com");
            admin.setNickname("관리자");
            admin.setPhone("010-1234-5678");
            admin.setPostcode("12345");
            admin.setAddress("관리자 주소");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
