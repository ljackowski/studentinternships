package com.ljackowski.studentinternships.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects(){
        return subjectRepository.findAll();
    }

    public Subject getSubjectBuId(long subjectId){
        return subjectRepository.findById(subjectId).get();
    }

    public void deleteSubjectById(long subjectId){
        subjectRepository.deleteById(subjectId);
    }

    public void addSubject(Subject subject){
        subjectRepository.save(subject);
    }

    public void updateSubject(Subject subject){
        subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjectsByFieldOfStudy(String fieldOfStudy){
        return subjectRepository.findAllByFieldOfStudy(fieldOfStudy);
    }

}
