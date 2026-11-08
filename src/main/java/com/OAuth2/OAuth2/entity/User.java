package com.OAuth2.OAuth2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String imageUrl;

    private String provider; // لمعرفة هل سجل عبر google أو github مستقبلاً

    private String providerId; // الـ ID الفريد الذي يرسله المزود للمستخدم
}