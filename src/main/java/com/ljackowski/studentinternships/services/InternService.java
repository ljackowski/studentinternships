package com.ljackowski.studentinternships.services;

import com.ljackowski.studentinternships.models.Coordinator;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.repositories.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InternService {
    private final InternRepository internRepository;

    @Autowired
    public InternService(InternRepository internRepository) {
        this.internRepository = internRepository;
    }

    public List<Intern> getAllInterns() {
        return internRepository.findAll();
    }

    public Intern getInternById(long internId) {
        return internRepository.findById(internId).get();
    }

    public void addIntern(Intern intern) {
        internRepository.save(intern);
    }

    public void deleteIntern(long internId) {
        internRepository.deleteInterNative(internId);
    }

    public void updateIntern(Intern intern) {
        internRepository.save(intern);
    }

    public void deleteAll() {
        internRepository.deleteAllNative();
    }

    public Intern getInternByStudent(Student student) {
        return internRepository.findByStudent(student);
    }

    public List<Intern> getInternsByCoordinator(Coordinator coordinator) {
        return internRepository.findAllByStudent_Coordinator(coordinator);
    }
}
