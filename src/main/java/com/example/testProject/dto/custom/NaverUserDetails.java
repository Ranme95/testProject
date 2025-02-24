package com.example.testProject.dto.custom;

import com.example.testProject.OAuth2.OAuth2UserInfo;

import java.util.Map;


public class NaverUserDetails implements OAuth2UserInfo {
    private Map<String,Object> attributes;

    public NaverUserDetails(Map<String,Object> attributes) {
    this.attributes =(Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

}
