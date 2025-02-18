package com.example.testProject.Repository;

import com.example.testProject.Entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test,Long> {
}
