package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
}
