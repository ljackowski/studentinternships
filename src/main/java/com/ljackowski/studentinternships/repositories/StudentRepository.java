package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByFieldOfStudy(String fieldOfStudy);
    Student findByEmail(String email);
    List<Student> findAllByFieldOfStudy(String fieldOfStudy);
    List<Student> findFirst20ByOrderByAverageGradeDesc();
    List<Student> findAllByCompany(Company company);
}


