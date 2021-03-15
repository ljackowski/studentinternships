package com.ljackowski.studentinternships.coordinator;


import com.ljackowski.studentinternships.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {
    Coordinator findByFieldOfStudy(String fieldOfStudy);
    List<Student> findAllByFieldOfStudy(String fieldOfStudy);
}
