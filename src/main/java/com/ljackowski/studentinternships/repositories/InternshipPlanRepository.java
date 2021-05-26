package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.InternshipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InternshipPlanRepository extends JpaRepository<InternshipPlan, Long> {
}
