package com.example.testProject.dto.custom;

import com.example.testProject.Entity.Partner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PartnerUserDetails implements UserDetails {

    private final Partner partner;

    public PartnerUserDetails(Partner partner) {
        this.partner = partner;
    }

    @Override
    public String getUsername() {
        return partner.getPartnerId();
    }

    @Override
    public String getPassword() {
        return partner.getPartnerPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_"+partner.getRole().name();
            }
        });
        return collection;
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
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
