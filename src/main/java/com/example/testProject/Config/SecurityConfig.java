package com.example.testProject.Config;

import com.example.testProject.Common.MemberRole;
import com.example.testProject.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //접근 권한 설정
        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/oauth-login/admin")
                .hasRole(MemberRole.ADMIN.name())
                .requestMatchers("/oauth-login.info").authenticated().anyRequest().permitAll());


        http.oauth2Login((auth) -> auth.loginPage("/oauth/login")
                .defaultSuccessUrl("/oauth/login/info")
                .failureUrl("/login")
                .permitAll()
        );


        return http.build();

    }

}
