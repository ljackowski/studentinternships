package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {
    Intern findByStudent(Student student);
    List<Intern> findAllByStudent_Coordinator(Coordinator coordinator);
}
