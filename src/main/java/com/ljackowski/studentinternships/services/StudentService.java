package com.ljackowski.studentinternships.services;

import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public Student getStudentById(long userId){
        return studentRepository.findById(userId).get();
    }

    public void addStudent(Student student){
        studentRepository.save(student);
    }

    public void updateStudent(Student student){
        studentRepository.save(student);
    }

    public void deleteStudentById(long userId){
        studentRepository.deleteById(userId);
    }

    public Student getStudentByEmail(String email){
        return studentRepository.findByEmail(email);
    }

    public List<Student> getAllStudentsByFieldOfStudy(String fieldOfStudy){
        return studentRepository.findAllByFieldOfStudy(fieldOfStudy);
    }

    public List<Student> getFirst20StudentsByAvgGrade(){
        return studentRepository.findFirst20ByOrderByAverageGradeDesc();
    }

    public List<Student> getAllStudentsInCompany(Company company){
        return studentRepository.findAllByCompany(company);
    }
}
