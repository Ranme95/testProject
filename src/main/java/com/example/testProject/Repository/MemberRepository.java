package com.example.testProject.Repository;

import com.example.testProject.Entity.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUserId(String userId);

//    @Override
//    List<Member> findAll();
}
