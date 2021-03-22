package com.ljackowski.studentinternships.traineejournal;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface TraineeJournalRepository extends JpaRepository<TraineeJournal,Long>{
    List<TraineeJournal> findAllByStudentUserId(long studentId);
}
