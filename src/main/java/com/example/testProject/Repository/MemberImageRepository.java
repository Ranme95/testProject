package com.example.testProject.Repository;

import com.example.testProject.Entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberImageRepository extends JpaRepository<MemberImage,Long> {
    Optional<MemberImage> findByMemberId(Long memberId);
}
