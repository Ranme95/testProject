package com.example.testProject.Repository;

import com.example.testProject.Entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner,Long> {
    Optional<Partner> findByPartnerId(String partnerId);
}
