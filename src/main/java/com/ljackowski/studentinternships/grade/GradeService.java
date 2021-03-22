package com.ljackowski.studentinternships.grade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> getGrades(){
        return gradeRepository.findAll();
    }

    public Grade getGradeById(long gradeId){
        return gradeRepository.findById(gradeId).get();
    }

    public void deleteGradeById(long gradeId){
        gradeRepository.deleteById(gradeId);
    }

    public Grade addGrade(Grade grade){
        return gradeRepository.save(grade);
    }

    public void updateGrade(Grade grade){
        gradeRepository.save(grade);
    }

    public List<Grade> getStudentsGrades(long studentId){
        return gradeRepository.findAllByStudentUserId(studentId);
    }
}
