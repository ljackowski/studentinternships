package com.ljackowski.studentinternships.grade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade,Long> {
    List<Grade> findAllByStudentUserId(long studentId);
}
