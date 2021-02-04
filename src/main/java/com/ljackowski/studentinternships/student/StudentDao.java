package com.ljackowski.studentinternships.student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    int insertStudent(Student student);

    List<Student> selectAllStudents();

    Optional<Student> selectStudentByIndex(int studentIndex);

    int deleteStudentById(int studentIndex);

    int updateStudentById(int studentIndex, Student student);
}


