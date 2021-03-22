package com.ljackowski.studentinternships.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByFieldOfStudy(String fieldOfStudy);
    Student findByEmail(String email);
}


