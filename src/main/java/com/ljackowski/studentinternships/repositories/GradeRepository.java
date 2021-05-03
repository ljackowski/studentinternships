package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GradeRepository extends JpaRepository<Grade,Long> {
    List<Grade> findAllByStudentUserId(long studentId);
}
