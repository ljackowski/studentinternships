package com.ljackowski.studentinternships.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("studentService")
public class StudentService {
    private final StudentDao studentDao;

    @Autowired
    public StudentService(@Qualifier("StudentDao") StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public int addStudent(Student student, int user_id){
        return studentDao.insertStudent(student, user_id);
    }

    public List<Student> getAllStudents(){
        return studentDao.selectAllStudents();
    }

    public Optional<Student> getStudentByIndex(int studentIndex){
        return studentDao.selectStudentByIndex(studentIndex);
    }

    public int deleteStudent(int nrIndeksu){
        return studentDao.deleteStudentById(nrIndeksu);
    }

    public int updateStudent(int nrIndeksu, Student newStudent){
        return studentDao.updateStudentById(nrIndeksu, newStudent);
    }
}
