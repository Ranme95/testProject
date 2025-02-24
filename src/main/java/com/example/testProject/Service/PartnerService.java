package com.example.testProject.Service;

import com.example.testProject.Common.MemberRole;
import com.example.testProject.Entity.Partner;
import com.example.testProject.Repository.PartnerRepository;
import com.example.testProject.dto.PartnerDto;
import com.example.testProject.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerService {

    private final PartnerRepository partnerRepository;

    private final PasswordEncoder passwordEncoder;

    public void savePartner(PartnerDto partnerDto){

        Partner partner = Partner.builder()
                .partnerId(partnerDto.getPartnerId())
                .partnerPassword(passwordEncoder.encode(partnerDto.getPartnerPassword()))
                .role(MemberRole.PARTNER)
                .build();

        partnerRepository.save(partner);

    }

    public Boolean partnerLogin(PartnerDto partnerDto, HttpServletRequest request){
        String id = partnerDto.getPartnerId();
        String password = partnerDto.getPartnerPassword();

        Optional<Partner> optionalPartner = partnerRepository.findByPartnerId(id);

        if(optionalPartner.isEmpty()) return false

        Partner partner = optionalPartner.get();

        if(passwordEncoder.matches(password,partner.getPartnerPassword())){

            HttpSession session =request.getSession(true);
            session.setAttribute("partnerId",partner.getId());

            return true;
        }
        return false;

    }
}
