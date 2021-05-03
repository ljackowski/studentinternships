package com.ljackowski.studentinternships.repositories;


import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {
    Coordinator findByFieldOfStudy(String fieldOfStudy);
    List<Student> findAllByFieldOfStudy(String fieldOfStudy);
}
