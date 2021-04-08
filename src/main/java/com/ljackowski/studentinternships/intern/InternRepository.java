package com.ljackowski.studentinternships.intern;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternRepository extends JpaRepository<Intern, Long> {
    public void deleteByStudent(Student student);
}
