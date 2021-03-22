package com.ljackowski.studentinternships.subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByFieldOfStudy(String fieldOfStudy);
}
