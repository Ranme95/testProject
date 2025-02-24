package com.example.testProject.Service;

import com.example.testProject.Entity.Partner;
import com.example.testProject.Repository.PartnerRepository;
import com.example.testProject.dto.PartnerJoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public void savePartner(PartnerJoinDto partnerJoinDto){

        Partner partner = Partner.builder()
                .partnerId(partnerJoinDto.getPartnerId())
                .partnerPassword(partnerJoinDto.getPartnerPassword())
                .build();

        partnerRepository.save(partner);

    }


}
