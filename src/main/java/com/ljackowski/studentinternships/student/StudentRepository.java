package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByFieldOfStudy(String fieldOfStudy);
    Student findByEmail(String email);
    List<Student> findAllByFieldOfStudy(String fieldOfStudy);
    List<Student> findFirst20ByOrderByAverageGradeDesc();
    List<Student> findAllByCompany(Company company);
}


