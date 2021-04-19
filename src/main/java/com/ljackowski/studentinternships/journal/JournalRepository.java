package com.ljackowski.studentinternships.journal;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface JournalRepository extends JpaRepository<Journal,Long>{
    List<Journal> findAllByStudentUserId(long studentId);

}
