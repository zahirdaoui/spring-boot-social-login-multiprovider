package com.OAuth2.OAuth2.service;

import com.OAuth2.OAuth2.entity.User;
import com.OAuth2.OAuth2.entity.UserProvider;
import com.OAuth2.OAuth2.repo.UserProviderRepository;
import com.OAuth2.OAuth2.repo.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;

    public CustomOAuth2UserService(UserRepository userRepository, UserProviderRepository userProviderRepository) {
        this.userRepository = userRepository;
        this.userProviderRepository = userProviderRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        String name = null;
        String picture = null;
        String providerId = null;

        if (registrationId.equals("google")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            picture = oAuth2User.getAttribute("picture");
            providerId = oAuth2User.getAttribute("sub");
        }
        else if (registrationId.equals("facebook")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            providerId = oAuth2User.getAttribute("id");

            Map<String, Object> pictureObj = oAuth2User.getAttribute("picture");
            if (pictureObj != null && pictureObj.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) pictureObj.get("data");
                picture = (String) data.get("url");
            }
        }
        else if (registrationId.equals("github")) {

            Object idObj = oAuth2User.getAttribute("id");
            if (idObj != null) {
                providerId = idObj.toString();
            }
            name = oAuth2User.getAttribute("name");
            if (name == null) {
                name = oAuth2User.getAttribute("login"); //
            }
            email = oAuth2User.getAttribute("email");
            if (email == null) {
                email = oAuth2User.getAttribute("login") + "@github.com";
            }
            picture = oAuth2User.getAttribute("avatar_url");
        }
        else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }


        UserProvider userProvider = userProviderRepository.findByProviderAndProviderId(registrationId, providerId)
                .orElse(null);

        User user;

        if (userProvider != null) {
            user = userProvider.getUser();
            user.setName(name);
            user.setImageUrl(picture);
            userRepository.save(user);
        } else {
            user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                user = User.builder()
                        .email(email)
                        .name(name)
                        .imageUrl(picture)
                        .build();
                user = userRepository.save(user);
            }

            UserProvider newProvider = UserProvider.builder()
                    .provider(registrationId)
                    .providerId(providerId)
                    .user(user)
                    .build();
            userProviderRepository.save(newProvider);
        }

        return oAuth2User;
    }
}