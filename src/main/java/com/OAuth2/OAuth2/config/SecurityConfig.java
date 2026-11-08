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

    private final CustomOAuth2UserService        customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/webjars/**,","/login", "/oauth2/**", "/css/**", "/js/**").permitAll() // السماح للجميع بزيارة الصفحة الرئيسية
                        .anyRequest().authenticated() // باقي الروابط تتطلب تسجيل دخول
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) )/// بعد نجاح تسجيل الدخول، وجهه إلى صفحة /home
                ).logout(logout -> logout
                           .logoutUrl("/logout")         // الرابط الافتراضي للـ Logout
                           .logoutSuccessUrl("/")        // أين يذهب المستخدم بعد تسجيل الخروج بنجاح
                           .invalidateHttpSession(true)  // تدمير الجلسة
                           .clearAuthentication(true)    // مسح المصادقة
                           .permitAll()
                   );

        return http.build();
    }
}