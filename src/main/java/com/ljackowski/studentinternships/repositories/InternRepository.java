package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {
    Intern findByStudent(Student student);
    List<Intern> findAllByStudent_Coordinator(Coordinator coordinator);

    @Query(value = "delete from Intern i where i.internId = ?1")
    @Transactional
    @Modifying
    void deleteInterNative(long internId);

    @Query(value = "delete from Intern")
    @Transactional
    @Modifying
    void deleteAllNative();
}
