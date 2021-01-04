package com.ljackowski.studentinternships.dao;

import com.ljackowski.studentinternships.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("StudentDao")
public class StudentDataAccessService implements StudentDao {
    private final JdbcTemplate jdbcTemplate;
    private static List<Student> DB = new ArrayList<>();

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertStudent(UUID studentId, Student student) {
        DB.add(new Student(studentId, student.getImie(), student.getNazwisko(), student.getEmail(), student.getNrTelefonu(), student.getNrIndeksu(), student.getKierunekStudiow(), student.getStopien(), student.getSredniaOcen()));
        return 0;
    }

    @Override
    public List<Student> selectAllStudents() {
        String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, (resultSet, i) ->{
            UUID studentId = UUID.fromString(resultSet.getString("id"));
            String imie = resultSet.getString("imie");
            String nazwisko = resultSet.getString("nazwisko");
            String email = resultSet.getString("email");
            String nrtelefonu = resultSet.getString("nrtelefonu");
            String nrindeksu = resultSet.getString("nrindeksu");
            String kierunekstudiow = resultSet.getString("kierunekstudiow");
            String stopien = resultSet.getString("stopien");
            double sredniaocen = resultSet.getDouble("sredniaocen");
            return new Student(studentId, imie, nazwisko, email, nrtelefonu, nrindeksu, kierunekstudiow, stopien, sredniaocen);
        });
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
