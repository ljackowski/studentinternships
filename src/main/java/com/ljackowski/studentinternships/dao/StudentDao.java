package com.ljackowski.studentinternships.dao;

import com.ljackowski.studentinternships.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentDao {
    int insertStudent(UUID studentId, Student student);

    default int insertStudent(Student student) {
        UUID studentId = UUID.randomUUID();
        return insertStudent(studentId, student);
    }

    List<Student> selectAllStudents();

    Optional<Student> selectStudentById(UUID studentId);

    int deleteStudentById(UUID studentId);

    int updateStudentById(UUID studentId, Student student);
}


