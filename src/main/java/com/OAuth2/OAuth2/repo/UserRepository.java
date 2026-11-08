package com.OAuth2.OAuth2.repo;


import com.OAuth2.OAuth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // للبحث عن المستخدم بواسطة البريد الإلكتروني لمعرفة هل هو مسجل مسبقاً أم لا
    Optional<User> findByEmail(String email);
}