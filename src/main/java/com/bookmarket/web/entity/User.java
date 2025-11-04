package com.bookmarket.web.entity;

import com.bookmarket.web.dto.UserSignupDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String postcode;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public enum Role {
        USER, ADMIN
    }

    public User(UserSignupDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.nickname = dto.getNickname();
        this.phone = dto.getPhone();
        this.postcode = dto.getPostcode();
        this.address = dto.getAddress();
        this.role = Role.USER;
    }
}
