package com.example.testProject.Service;

import com.example.testProject.Entity.Member;
import com.example.testProject.OAuth2.CustomUserDetails;
import com.example.testProject.Repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final HttpServletRequest httpServletRequest;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByUserId(username);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("memberId",member.getId());
            return new CustomUserDetails(member);
        }

        return null;
    }
}
