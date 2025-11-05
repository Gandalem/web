package com.bookmarket.web.repository;

import com.bookmarket.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이름으로 User를 찾는 메서드 (필요시 추가)
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
}
