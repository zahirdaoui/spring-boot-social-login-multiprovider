package com.OAuth2.OAuth2.repo;

import com.OAuth2.OAuth2.entity.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderRepository  extends JpaRepository<UserProvider, Long> {

    Optional<UserProvider> findByProviderAndProviderId(String registrationId, String providerId);
}
