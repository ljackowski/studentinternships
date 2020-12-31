package com.ljackowski.studentinternships.service;

import com.ljackowski.studentinternships.dao.StudentDao;
import com.ljackowski.studentinternships.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    private final StudentDao studentDao;

    @Autowired
    public StudentService(@Qualifier("StudentDao") StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public int addStudent(Student student){
        return studentDao.insertStudent(student);
    }

    public List<Student> getAllStudents(){
        return studentDao.selectAllStudents();
    }

    public Optional<Student> getStudentById(UUID studentId){
        return studentDao.selectStudentById(studentId);
    }

    public int deleteStudent(UUID studentId){
        return studentDao.deleteStudentById(studentId);
    }

    public int updateStudent(UUID studentID, Student newStudent){
        return studentDao.updateStudentById(studentID, newStudent);
    }
}
