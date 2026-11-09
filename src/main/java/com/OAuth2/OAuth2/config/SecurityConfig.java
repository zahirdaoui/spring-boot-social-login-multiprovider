package com.OAuth2.OAuth2.config;
import com.OAuth2.OAuth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http.csrf(csrf -> csrf.disable())
                   .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/webjars/**","/login", "/oauth2/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) )
                ).logout(logout -> logout
                           .logoutUrl("/logout")
                           .logoutSuccessUrl("/")
                           .invalidateHttpSession(true)
                           .clearAuthentication(true)
                           .permitAll()
                   );

        return http.build();
    }
}