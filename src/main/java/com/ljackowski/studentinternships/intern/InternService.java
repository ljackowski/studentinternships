package com.ljackowski.studentinternships.intern;

import com.ljackowski.studentinternships.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternService {
    private final InternRepository internRepository;

    @Autowired
    public InternService(InternRepository internRepository) {
        this.internRepository = internRepository;
    }

    public List<Intern> getAllInterns(){
        return internRepository.findAll();
    }

    public Intern getInternById(long internId){
        return internRepository.findById(internId).get();
    }

    public void addIntern(Intern intern){
        internRepository.save(intern);
    }


    public void deleteInternById(long internId){
        internRepository.deleteById(internId);
    }

    public void updateIntern(Intern intern){
        internRepository.save(intern);
    }
    public void deleteAll(){
        internRepository.deleteAll();
    }
    public void deleteInternByStudent(Student student){
        internRepository.deleteByStudent(student);
    };

}