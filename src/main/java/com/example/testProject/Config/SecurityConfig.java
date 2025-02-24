package com.example.testProject.Config;

import com.example.testProject.Common.MemberRole;
import com.example.testProject.Repository.MemberRepository;
import com.example.testProject.Repository.PartnerRepository;
import com.example.testProject.Service.CustomUserDetailsService;
import com.example.testProject.Service.PartnerUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PartnerRepository partnerRepository;

    private final HttpServletRequest request;

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;


    @Bean
    @Order(1)
    protected SecurityFilterChain memberFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher(new AntPathRequestMatcher("/"));

        //csrf 사용 안함
        http.csrf(AbstractHttpConfigurer::disable);

        http.authenticationProvider(memberDaoAuthenticationProvider());

        //접근 권한 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/my-page","/update", "/delete", "/logout").hasAnyRole(MemberRole.USER.name(),MemberRole.ADMIN.name())
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

    @Bean
    @Order(2)
    protected SecurityFilterChain partnerFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/")));

        //csrf 사용 안함
        http.csrf(AbstractHttpConfigurer::disable);

        http.authenticationProvider(partnerDaoAuthenticationProvider());

        //접근 권한 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/partner/**").hasRole(MemberRole.PARTNER.name())
                .requestMatchers("/member/list").hasRole(MemberRole.ADMIN.name())
                .anyRequest().permitAll()
        );

        //폼 로그인
        //필터가 login 처리하므로 컨트롤러 따로 필요 X
        http.formLogin((auth)->auth
                .loginPage("/partner/login")
                .loginProcessingUrl("/partner/login")
                .failureUrl("/partner/login?error")
                .usernameParameter("partnerId")
                .passwordParameter("partnerPassword")
                .defaultSuccessUrl("/partner/")
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

    @Bean
    DaoAuthenticationProvider memberDaoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new CustomUserDetailsService(memberRepository,request));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    DaoAuthenticationProvider partnerDaoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new PartnerUserDetailsService(partnerRepository,request));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
}
