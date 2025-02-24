package com.example.testProject.Service;


import com.example.testProject.Common.MemberRole;
import com.example.testProject.Entity.Member;
import com.example.testProject.Entity.MemberImage;
import com.example.testProject.dto.custom.CustomOauth2UserDetails;
import com.example.testProject.dto.custom.GoogleUserDetails;
import com.example.testProject.dto.custom.NaverUserDetails;
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

        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String loginId = provider + "_" + providerId;

        Optional<Member> findMember = memberRepository.findByUserId(loginId);

        Member member;
        if (findMember.isEmpty()) {
            member = Member.builder()
                    .userId(loginId)
                    .provider(provider)
                    .providerId(providerId)
                    .role(MemberRole.USER)
                    .build();

            MemberImage memberImage = MemberImage.builder()
                    .member(member)
                    .build();


            member.setMemberImage(memberImage);

            memberRepository.save(member);
        } else {
            member = findMember.get();
        }

        httpSession.setAttribute("memberId",member.getId());

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());

    }
}
