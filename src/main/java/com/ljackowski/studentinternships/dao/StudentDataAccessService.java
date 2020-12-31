package com.ljackowski.studentinternships.dao;

import com.ljackowski.studentinternships.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("StudentDao")
public class StudentDataAccessService implements StudentDao {
    private static List<Student> DB = new ArrayList<>();

    @Override
    public int insertStudent(UUID studentId, Student student) {
        DB.add(new Student(studentId, student.getImie(), student.getNazwisko(), student.getEmail(), student.getNrTelefonu(), student.getNrIndeksu(), student.getKierunekStudiow(), student.getStopien(), student.getSredniaOcen()));
        return 0;
    }

    @Override
    public List<Student> selectAllStudents() {
        return DB;
    }

    @Override
    public Optional<Student> selectStudentById(UUID studentId) {
        return DB.stream().filter(student -> student.getStudentId().equals(studentId)).findFirst();
    }

    @Override
    public int deleteStudentById(UUID studentId) {
        Optional<Student> studentMaybe = selectStudentById(studentId);
        if (!studentMaybe.isPresent()) {
            return 0;
        }
        DB.remove(studentMaybe.get());
        return 1;
    }

    @Override
    public int updateStudentById(UUID studentId, Student studentToUpdate) {
        return selectStudentById(studentId)
                .map(student -> {
                    int indexOfStudentToUpdate = DB.indexOf(student);
                    if (indexOfStudentToUpdate >= 0) {
                        DB.set(indexOfStudentToUpdate, new Student(studentId, studentToUpdate.getImie(), studentToUpdate.getNazwisko(), studentToUpdate.getEmail(), studentToUpdate.getNrTelefonu(), studentToUpdate.getNrIndeksu(), studentToUpdate.getKierunekStudiow(), studentToUpdate.getStopien(), studentToUpdate.getSredniaOcen()));
                        return 1;
                    }
                    return 0;
                }).orElse(0);
    }
}
