package com.example.testProject.Config;

import com.example.testProject.Common.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //접근 권한 설정
        //어떤 요청이든 접근 가능
//        http.authorizeHttpRequests((auth)->auth.anyRequest().permitAll()
//        );


        http.csrf(AbstractHttpConfigurer::disable);


        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/my-page","/update", "/delete", "/logout").hasRole(MemberRole.USER.name())
                .anyRequest().permitAll()
        );

        http.formLogin((auth)->auth
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .defaultSuccessUrl("/my-page")
                .permitAll()
        );


        http.oauth2Login((auth) -> auth
                .loginPage("/login")
                .defaultSuccessUrl("/oauth/login/info")
                .failureUrl("/login")
                .permitAll()
        );


        return http.build();

    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//
//        return new BCryptPasswordEncoder();
//    }

}
