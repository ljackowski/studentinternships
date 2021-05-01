package com.ljackowski.studentinternships.repositories;


import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {
    Coordinator findByFieldOfStudy(String fieldOfStudy);
    List<Student> findAllByFieldOfStudy(String fieldOfStudy);
}
