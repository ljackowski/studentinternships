package com.ljackowski.studentinternships.intern;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternRepository extends JpaRepository<Intern, Long> {
    void deleteByStudent(Student student);
    Intern findByStudent(Student student);
}
