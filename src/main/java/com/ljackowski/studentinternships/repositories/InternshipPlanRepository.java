package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.InternshipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipPlanRepository extends JpaRepository<InternshipPlan, Long> {
    List<InternshipPlan> findAllByIntern(Intern intern);
}
