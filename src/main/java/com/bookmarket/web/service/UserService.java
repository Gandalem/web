package com.bookmarket.web.service;

import com.bookmarket.web.entity.User;
import com.bookmarket.web.entity.User.Role;
import com.bookmarket.web.repository.UserRepository;
import com.bookmarket.web.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    @Transactional
    public User signup(UserSignupDto userSignupDto) {
        if (userRepository.findByUsername(userSignupDto.getUsername()) != null) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }
        if (userRepository.findByEmail(userSignupDto.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User();
        user.setUsername(userSignupDto.getUsername());
        user.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));
        user.setEmail(userSignupDto.getEmail());
        user.setRole(Role.USER); // 기본 역할은 USER

        return userRepository.save(user);
    }
}
