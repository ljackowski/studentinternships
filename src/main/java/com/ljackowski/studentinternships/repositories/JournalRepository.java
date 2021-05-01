package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal,Long>{
    List<Journal> findAllByStudentUserIdOrderByDay(long studentId);

}
