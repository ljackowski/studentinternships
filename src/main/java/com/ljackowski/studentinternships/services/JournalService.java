package com.ljackowski.studentinternships.services;

import com.ljackowski.studentinternships.models.Journal;
import com.ljackowski.studentinternships.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class JournalService {
    private final JournalRepository journalRepository;

    @Autowired
    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public List<Journal> getAllTraineeJournals() {
        return journalRepository.findAll();
    }

    public void addEntry(Journal journal) {
        journalRepository.save(journal);
    }

    public void deleteEntryById(long entryId) {
        journalRepository.deleteById(entryId);
    }

    public Journal getEntryById(long entryId) {
        return journalRepository.findById(entryId).get();
    }

    public void editEntry(Journal entry) {
        journalRepository.save(entry);
    }

    public List<Journal> getAllEntriesOfStudent(long studentId) {
        return journalRepository.findAllByStudentUserIdOrderByDay(studentId);
    }

    public List<Journal> sortByDate(List<Journal> journalList) {
        journalList.sort(new Comparator<Journal>() {
            @Override
            public int compare(Journal o1, Journal o2) {
                return o1.getDay().compareTo(o2.getDay());
            }
        });
        return journalList;
    }
}
