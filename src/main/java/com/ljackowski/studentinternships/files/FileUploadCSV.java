package com.ljackowski.studentinternships.files;

import com.ljackowski.studentinternships.models.*;
import com.ljackowski.studentinternships.services.CompanyService;
import com.ljackowski.studentinternships.services.CoordinatorService;
import com.ljackowski.studentinternships.services.StudentService;
import com.ljackowski.studentinternships.services.SubjectService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Random;

public class FileUploadCSV {

    private final PasswordEncoder passwordEncoder;
    private Reader reader;
    private CSVParser csvParser;

    public FileUploadCSV(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public CSVParser createParser(Reader reader) throws IOException {
        return new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
    }
    public void addCoordinatorsFromFile(MultipartFile file, StudentService studentService, CoordinatorService coordinatorService) throws IOException {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        csvParser = createParser(reader);
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        for (CSVRecord csvRecord : csvRecords) {
            Coordinator coordinator = new Coordinator(
                    csvRecord.get("EMAIL"),
                    passwordEncoder.encode(csvRecord.get("PASSWORD")),
                    csvRecord.get("FIRST_NAME"),
                    csvRecord.get("LAST_NAME"),
                    csvRecord.get("FIELD_OF_STUDY"),
                    csvRecord.get("TELEPHONE_NUMBER"),
                    csvRecord.get("POSITION")
            );
            for (Student student: studentService.getAllStudentsByFieldOfStudy(coordinator.getFieldOfStudy())){
                student.setCoordinator(coordinator);
            }
            coordinatorService.addCoordinator(coordinator);
        }
    }

    public void addStudentsFromFile(MultipartFile file, CoordinatorService coordinatorService, StudentService studentService, SubjectService subjectService) throws IOException {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        csvParser = createParser(reader);
        Random random = new Random();
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        double averageGrade = 0;
        for (CSVRecord csvRecord : csvRecords) {
            Student student = new Student(
                    csvRecord.get("EMAIL"),
                    passwordEncoder.encode(csvRecord.get("PASSWORD")),
                    Integer.parseInt(csvRecord.get("STUDENT_INDEX")),
                    csvRecord.get("FIRST_NAME"),
                    csvRecord.get("LAST_NAME"),
                    csvRecord.get("TELEPHONE_NUMBER"),
                    csvRecord.get("FIELD_OF_STUDY"),
                    csvRecord.get("DEGREE"),
                    0,
                    new Address(
                            csvRecord.get("CITY"),
                            csvRecord.get("STREET"),
                            csvRecord.get("BUILDING_NUMBER"),
                            csvRecord.get("ZIPCODE"),
                            csvRecord.get("POST_OFFICE"),
                            csvRecord.get("APARTMENT_NUMBER")
                    )
            );
            student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
            List<Subject> subjects = subjectService.getAllSubjectsByFieldOfStudy(student.getFieldOfStudy());
            for (Subject subject : subjects) {
                student.addGrade(new Grade(student, subject, random.ints(2,4).findFirst().getAsInt() + 0.5));
            }
            for (Grade grade : student.getGradeList()){
                averageGrade += grade.getGradeNumber();
            }
            averageGrade /= student.getGradeList().size();
            student.setAverageGrade(averageGrade);
            studentService.addStudent(student);
        }
    }



    public void addSubjectsFromFile(MultipartFile file, SubjectService subjectService, StudentService studentService) throws IOException {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        csvParser = createParser(reader);
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        for (CSVRecord csvRecord : csvRecords){
            Subject subject = new Subject(
                    csvRecord.get("SUBJECT_NAME"),
                    csvRecord.get("FIELD_OF_STUDY")
            );
            for (Student student : studentService.getAllStudentsByFieldOfStudy(subject.getFieldOfStudy())){
                student.addGrade(new Grade(student,subject));
            }
        }
    }

    public void addCompaniesFromFile(MultipartFile file, CompanyService companyService) throws IOException {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        csvParser = createParser(reader);
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        for (CSVRecord csvRecord : csvRecords){;
        }
    }
}
