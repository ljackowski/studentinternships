package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade,Long> {
    List<Grade> findAllByStudentUserId(long studentId);
}
