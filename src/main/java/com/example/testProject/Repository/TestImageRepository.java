package com.example.testProject.Repository;

import com.example.testProject.Entity.Test;
import com.example.testProject.Entity.TestImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestImageRepository extends JpaRepository<TestImage,Long> {
    Optional<TestImage> findByTestId(Long testId);
}
