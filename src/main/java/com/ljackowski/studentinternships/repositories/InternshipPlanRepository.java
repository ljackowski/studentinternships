package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.InternshipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InternshipPlanRepository extends JpaRepository<InternshipPlan, Long> {
    List<InternshipPlan> findAllByIntern(Intern intern);
}
