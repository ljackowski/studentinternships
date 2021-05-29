package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    List<Student> findAllByFieldOfStudy(String fieldOfStudy);
    List<Student> findFirst20ByOrderByAverageGradeDesc();
    List<Student> findAllByCompany(Company company);
}


