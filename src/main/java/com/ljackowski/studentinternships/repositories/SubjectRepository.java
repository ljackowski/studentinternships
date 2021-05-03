package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByFieldOfStudy(String fieldOfStudy);
}
