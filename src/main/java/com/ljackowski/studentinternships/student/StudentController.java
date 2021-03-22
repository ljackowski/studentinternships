package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.coordinator.CoordinatorService;
import com.ljackowski.studentinternships.documentsgeneration.OrganizationAgreement;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.ljackowski.studentinternships.grade.Grade;
import com.ljackowski.studentinternships.grade.GradeService;
import com.ljackowski.studentinternships.subject.Subject;
import com.ljackowski.studentinternships.subject.SubjectService;
import com.ljackowski.studentinternships.traineejournal.TraineeJournal;
import com.ljackowski.studentinternships.traineejournal.TraineeJournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final CoordinatorService coordinatorService;
    private final GradeService gradeService;
    private final TemplateEngine templateEngine;
    private final TraineeJournalService traineeJournalService;
    private final SubjectService subjectService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    public StudentController(StudentService studentService, CoordinatorService coordinatorService, GradeService gradeService, TemplateEngine templateEngine, TraineeJournalService traineeJournalService, SubjectService subjectService) {
        this.studentService = studentService;
        this.coordinatorService = coordinatorService;
        this.gradeService = gradeService;
        this.templateEngine = templateEngine;
        this.traineeJournalService = traineeJournalService;
        this.subjectService = subjectService;
    }

    @RequestMapping(path = "/studentProfile")
    public String studentProfile(Model model) {
        model.addAttribute("studentProfile", model);
        return "userProfile";
    }

    @RequestMapping("/list")
    public String getAllStudents(Model model) {
        List<Student> studentsList = studentService.getStudents();
        for (Student student : studentsList) {
            student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
            studentService.updateStudent(student);
        }
        model.addAttribute("students", studentsList);
        return "studentsList";
    }

    @RequestMapping("/student/{userId}")
    public Student getStudent(@PathVariable(name = "userId") long userId) {
        return studentService.getStudentById(userId);
    }

    @GetMapping("/addStudent")
    public String addStudentForm(Model model) {
        model.addAttribute("addStudentForm", new Student());
        return "addStudentForm";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student) {
        student.setRole("student".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        String email = student.getEmail();
        studentService.addStudent(student);

        Student studentAfterCreation = studentService.getStudentByEmail(email);
        List<Subject> studentsSubjectList = subjectService.getAllSubjectsByFieldOfStudy(studentAfterCreation.getFieldOfStudy());
        for (Subject subject : studentsSubjectList){
            studentAfterCreation.addGrade(gradeService.addGrade(new Grade(studentAfterCreation, subject, 0)));
        }

        return "redirect:/students/list";
    }

    @GetMapping(path = "/delete")
    public String deleteStudentById(@RequestParam("userId") long userId) {
        studentService.deleteStudentById(userId);
        return "redirect:/students/list";
    }

    @GetMapping("/edit/{userId}")
    public String editStudentForm(@PathVariable(value = "userId") int userId, Model model) {
        Student student = studentService.getStudentById(userId);
        model.addAttribute("editStudentForm", student);
        return "editStudentForm";
    }

    @PostMapping("/edit/{userId}")
    public String editStudent(@ModelAttribute Student student) {
        student.setRole("student".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        Student student1 = studentService.getStudentById(student.getUserId());
        if(!student.getFieldOfStudy().equals(student1.getFieldOfStudy())){
            List<Grade> gradeList = gradeService.getStudentsGrades(student.getUserId());
            for (Grade grade : gradeList){
                gradeService.deleteGradeById(grade.getGradeId());
            }
            List<Subject> studentsSubjectList = subjectService.getAllSubjectsByFieldOfStudy(student.getFieldOfStudy());
            for (Subject subject : studentsSubjectList){
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
            }
        }
        studentService.updateStudent(student);
        return "redirect:/students/list";
    }

    //Student trainee journal

    @GetMapping("/traineeJournal/{userId}")
    public String getTraineeJournalByStudentId(@PathVariable(name = "userId") long studentId, Model model) {
        model.addAttribute("traineeJournal", traineeJournalService.getAllEntriesOfStudent(studentId));
        return "traineeJournal";
    }

    @GetMapping("/addEntry/{userId}")
    public String addEntryForm(Model model, @PathVariable(name = "userId") long studentId) {
        Student student = studentService.getStudentById(studentId);
        TraineeJournal traineeJournal = new TraineeJournal();
        traineeJournal.setStudent(student);
        model.addAttribute("addNewEntryToTraineeJournalForm", traineeJournal);
        return "addNewEntryToTraineeJournalForm";
    }

    @PostMapping("/addEntry/{userId}")
    public String addEntry(@ModelAttribute TraineeJournal traineeJournal, @PathVariable(name = "userId") long studentId) {
        Student student = studentService.getStudentById(studentId);
        traineeJournal.addStudentToEntry(student);
        traineeJournalService.addEntry(traineeJournal);
        return "redirect:/students/traineeJournal/{userId}";
    }

    @GetMapping("/deleteEntry")
    public String deleteStudentEntryById(@RequestParam("entryId") long entryId) {
        long userId = traineeJournalService.getEntryById(entryId).getStudent().getUserId();
        traineeJournalService.deleteEntryById(entryId);
        return "redirect:/students/traineeJournal/" + userId;
    }

    @GetMapping("/editEntry/{entryId}")
    public String editStudentEntryForm(@PathVariable("entryId") long entryId, Model model) {
        TraineeJournal traineeJournal = traineeJournalService.getEntryById(entryId);
        model.addAttribute("editStudentEntryForm", traineeJournal);
        return "editStudentEntryForm";
    }

    @PostMapping("/editEntry/{entryId}")
    public String editStudentEntry(@ModelAttribute TraineeJournal traineeJournal) {
        long userId = traineeJournalService.getEntryById(traineeJournal.getEntryId()).getStudent().getUserId();
        traineeJournalService.editEntry(traineeJournal);
        return "redirect:/students/traineeJournal/" + userId;
    }


    //PDFS

    @GetMapping("/organization")
    public String organizationForm(Model model) {
        model.addAttribute("studentAgreementForm", new OrganizationAgreement());
        return "studentAgreementForm";
    }

    @PostMapping(path = "/organization")
    public ResponseEntity<?> getPDF(@ModelAttribute OrganizationAgreement organizationAgreement, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext);
        return pdfGeneration.generateQuestionnaire(request, response, "Umowa_o_organizacje_praktyki_zawodowej", organizationAgreement);
    }

    @RequestMapping(path = "/pdf")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext);
        return pdfGeneration.generatePDF(request, response, "Deklaracja_planowanej_praktyki_zawodowej");
    }

}
