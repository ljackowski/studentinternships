package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {
    Intern findByStudent(Student student);
}
