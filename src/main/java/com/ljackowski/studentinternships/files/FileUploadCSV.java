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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FileUploadCSV {

    private PasswordEncoder passwordEncoder;
    private Reader reader;
    private CSVParser csvParser;

    public FileUploadCSV() {
    }

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
        ArrayList<Double> grades = new ArrayList<>(Arrays.asList(2.0,3.0,3.5,4.0,4.5,5.0));
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
                student.addGrade(new Grade(student, subject, grades.get(random.ints(0,5).findFirst().getAsInt())));
            }
            for (Grade grade : student.getGradeList()){
                averageGrade += grade.getGradeNumber();
            }
            averageGrade /= student.getGradeList().size();
            if (averageGrade > 5 ) averageGrade = 5;
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
                student.addGrade(new Grade(student,subject, 0));
            }
            subjectService.addSubject(subject);
        }
    }

    public void addCompaniesFromFile(MultipartFile file, CompanyService companyService) throws IOException {
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        csvParser = createParser(reader);
        List<String> RepresentativeAsString;
        List<String> GuardiansAsString;
        List<String> GuardianAsString;
        ArrayList<Guardian> guardians = new ArrayList<>();
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        for (CSVRecord csvRecord : csvRecords){
            Company company = new Company(
                    csvRecord.get("COMPANY_NAME"),
                    Boolean.parseBoolean(csvRecord.get("PART_OF_INTERNSHIP")),
                    Integer.parseInt(csvRecord.get("FREE_SPACES")),
                    csvRecord.get("FIELD_OF_STUDY"),
                    LocalDate.parse(csvRecord.get("STARTING_DATE")),
                    LocalDate.parse(csvRecord.get("ENDING_DATE")),
                    new Address(
                            csvRecord.get("CITY"),
                            csvRecord.get("STREET"),
                            csvRecord.get("BUILDING_NUMBER"),
                            csvRecord.get("ZIP_CODE"),
                            csvRecord.get("POST_OFFICE"),
                            csvRecord.get("APARTMENT_NUMBER")
                    )
            );
            RepresentativeAsString = Arrays.asList(csvRecord.get("REPRESENTATIVE").split(","));
            Representative representative = new Representative(
                    RepresentativeAsString.get(0),
                    RepresentativeAsString.get(1),
                    RepresentativeAsString.get(2),
                    RepresentativeAsString.get(3),
                    RepresentativeAsString.get(4)
            );
            company.setRepresentative(representative);
            GuardiansAsString = Arrays.asList(csvRecord.get("GUARDIANS").split(";"));
            for (String s: GuardiansAsString){
                GuardianAsString = Arrays.asList(s.split(","));
                Guardian guardian = new Guardian(
                        GuardianAsString.get(0),
                        GuardianAsString.get(1),
                        GuardianAsString.get(2),
                        GuardianAsString.get(3),
                        GuardianAsString.get(4)
                );
                guardian.setCompany(company);
                guardians.add(guardian);
            }
            company.setGuardianList(guardians);
            companyService.addCompany(company);
        }
    }
}
