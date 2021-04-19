package com.ljackowski.studentinternships.journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalService {
    private final JournalRepository journalRepository;

    @Autowired
     public JournalService(JournalRepository journalRepository) {
          this.journalRepository = journalRepository;
     }

     public List<Journal> getAllTraineeJournals(){
         return journalRepository.findAll();
     }

     public void addEntry(Journal journal){
        journalRepository.save(journal);
     }

     public void deleteEntryById(long entryId){
        journalRepository.deleteById(entryId);
     }

     public Journal getEntryById(long entryId){
        return journalRepository.findById(entryId).get();
     }

     public void editEntry(Journal entry){
        journalRepository.save(entry);
     }

     public List<Journal> getAllEntriesOfStudent(long studentId){
        return journalRepository.findAllByStudentUserId(studentId);
     }
}
