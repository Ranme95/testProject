package com.example.testProject.Service;

import com.example.testProject.Entity.Partner;
import com.example.testProject.Repository.PartnerRepository;
import com.example.testProject.dto.custom.PartnerUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerUserDetailsService implements UserDetailsService {

    private final PartnerRepository partnerRepository;

    private final HttpServletRequest request;

    public PartnerUserDetailsService(PartnerRepository partnerRepository, HttpServletRequest request) {

        this.partnerRepository = partnerRepository;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Partner> optionalPartner = partnerRepository.findByPartnerId(username);

        if (optionalPartner.isEmpty()) return null;

        Partner partner = optionalPartner.get();

        HttpSession session = request.getSession(true);
        session.setAttribute("partnerId",partner.getId());
        return new PartnerUserDetails(partner);

    }
}
