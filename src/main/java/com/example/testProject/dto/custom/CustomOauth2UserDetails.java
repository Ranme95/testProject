package com.example.testProject.dto.custom;

import com.example.testProject.Entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOauth2UserDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;

    public CustomOauth2UserDetails(Member member,Map<String,Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String,Object> getAttributes(){
        return attributes;
    }

    @Override
    public String getName(){
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return member.getUserPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_"+ member.getRole().name();
            }
        });
        return collection;
    }
}
