package com.OAuth2.OAuth2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;   //  google, github, facebook
    private String providerId; // ID  (sub  or id)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}