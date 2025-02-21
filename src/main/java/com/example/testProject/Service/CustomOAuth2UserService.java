package com.example.testProject.Service;


import com.example.testProject.Common.MemberRole;
import com.example.testProject.Entity.Member;
import com.example.testProject.Entity.MemberImage;
import com.example.testProject.OAuth2.CustomOauth2UserDetails;
import com.example.testProject.OAuth2.GoogleUserDetails;
import com.example.testProject.OAuth2.OAuth2UserInfo;
import com.example.testProject.Repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();

        Optional<Member> findMember = memberRepository.findByUserId(loginId);

        Member member;
        if (findMember.isEmpty()) {
            member = Member.builder()
                    .userId(loginId)
                    .provider(provider)
                    .providerId(providerId)
                    .role(MemberRole.USER)
                    .email(email)
                    .build();


            memberRepository.save(member);
        } else {
            member = findMember.get();

            memberRepository.save(member);
        }

        httpSession.setAttribute("memberId",member.getId());

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());

    }
}
