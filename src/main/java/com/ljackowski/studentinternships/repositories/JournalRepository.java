package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JournalRepository extends JpaRepository<Journal,Long>{
    List<Journal> findAllByStudentUserIdOrderByDay(long studentId);

}
