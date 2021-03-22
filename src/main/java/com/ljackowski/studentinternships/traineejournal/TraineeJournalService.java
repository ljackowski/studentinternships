package com.ljackowski.studentinternships.traineejournal;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeJournalService {
    private final TraineeJournalRepository traineeJournalRepository;

    @Autowired
     public TraineeJournalService(TraineeJournalRepository traineeJournalRepository) {
          this.traineeJournalRepository = traineeJournalRepository;
     }

     public List<TraineeJournal> getAllTraineeJournals(){
         return traineeJournalRepository.findAll();
     }

     public void addEntry(TraineeJournal traineeJournal){
        traineeJournalRepository.save(traineeJournal);
     }

     public void deleteEntryById(long entryId){
        traineeJournalRepository.deleteById(entryId);
     }

     public TraineeJournal getEntryById(long entryId){
        return traineeJournalRepository.findById(entryId).get();
     }

     public void editEntry(TraineeJournal entry){
        traineeJournalRepository.save(entry);
     }

     public List<TraineeJournal> getAllEntriesOfStudent(long studentId){
        return traineeJournalRepository.findAllByStudentUserId(studentId);
     }
}
