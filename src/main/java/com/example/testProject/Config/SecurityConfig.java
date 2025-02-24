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

        //csrf 사용 안함
        http.csrf(AbstractHttpConfigurer::disable);

        //접근 권한 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/my-page","/update", "/delete", "/logout").hasAnyRole(MemberRole.USER.name(),MemberRole.ADMIN.name(),MemberRole.PARTNER.name())
                .requestMatchers("/member/list").hasRole(MemberRole.ADMIN.name())
                .anyRequest().permitAll()
        );

        //폼 로그인
        //필터가 login 처리하므로 컨트롤러 따로 필요 X
        http.formLogin((auth)->auth
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .defaultSuccessUrl("/my-page")
                .permitAll()
        );

        //oauth 로그인
        http.oauth2Login((auth) -> auth
                .loginPage("/login")
                .defaultSuccessUrl("/my-page")
                .failureUrl("/login")
                .authorizationEndpoint(authorization->authorization.baseUri("/oauth2/authorization"))
                .permitAll()
        );


        //로그아웃
        //필터가 세션 삭제해주므로 컨트롤러 따로 필요 X
        http.logout((auth)->auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
        );


        return http.build();

    }
}
