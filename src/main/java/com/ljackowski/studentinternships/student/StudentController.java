package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.address.AddressService;
import com.ljackowski.studentinternships.company.CompanyService;
import com.ljackowski.studentinternships.coordinator.CoordinatorService;
import com.ljackowski.studentinternships.documentsgeneration.OrganizationAgreement;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.ljackowski.studentinternships.grade.Grade;
import com.ljackowski.studentinternships.grade.GradeService;
import com.ljackowski.studentinternships.intern.Intern;
import com.ljackowski.studentinternships.intern.InternService;
import com.ljackowski.studentinternships.representative.RepresentativeService;
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
    private final InternService internService;
    private final AddressService addressService;
    private final CompanyService companyService;
    private final RepresentativeService representativeService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    public StudentController(StudentService studentService, CoordinatorService coordinatorService, GradeService gradeService,
                             TemplateEngine templateEngine, TraineeJournalService traineeJournalService, SubjectService subjectService,
                             InternService internService, AddressService addressService, CompanyService companyService, RepresentativeService representativeService) {
        this.studentService = studentService;
        this.coordinatorService = coordinatorService;
        this.gradeService = gradeService;
        this.templateEngine = templateEngine;
        this.traineeJournalService = traineeJournalService;
        this.subjectService = subjectService;
        this.internService = internService;
        this.addressService = addressService;
        this.companyService = companyService;
        this.representativeService = representativeService;
    }

    @RequestMapping("/list")
    public String getAllStudents(Model model) {
        List<Student> studentsList = studentService.getStudents();
        for (Student student : studentsList) {
            student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
            studentService.updateStudent(student);
        }
        model.addAttribute("students", studentsList);
        return "lists/studentsList";
    }

    @GetMapping("/student/{userId}")
    public String studentProfileForm(@PathVariable(name = "userId") long userId, Model model) {
        Student student = studentService.getStudentById(userId);
        if (student.getRole().equals("STUDENT")){
            if (student.getCompany() == null) {
                model.addAttribute("student", student);
                return "profiles/studentProfileBeforeSettingCompany";
            } else {
                model.addAttribute("student", student);
                return "profiles/studentProfile";
            }
        }
        else if (student.getRole().equals("INTERN")){
            Intern intern = internService.getInternByStudent(student);
            return "redirect:/interns/intern/" + intern.getInternId();
        }
        return "";
    }

    @PostMapping("/student/{userId}")
    public String studentProfile(@ModelAttribute Student student) {
        Student studentBeforeUpdate = studentService.getStudentById(student.getUserId());
        student.setUserId(studentBeforeUpdate.getUserId());
        student.setPassword(studentBeforeUpdate.getPassword());
        student.setEmail(studentBeforeUpdate.getEmail());
        student.setRole(studentBeforeUpdate.getRole());
        student.setFirstName(studentBeforeUpdate.getFirstName());
        student.setLastName(studentBeforeUpdate.getLastName());
        student.setTelephoneNumber(studentBeforeUpdate.getTelephoneNumber());
        student.setStudentIndex(studentBeforeUpdate.getStudentIndex());
        student.setFieldOfStudy(studentBeforeUpdate.getFieldOfStudy());
        student.setDegree(studentBeforeUpdate.getDegree());
        student.setAverageGrade(studentBeforeUpdate.getAverageGrade());
        student.setAddress(studentBeforeUpdate.getAddress());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        companyService.addCompany(student.getCompany());
        addressService.addAddress(student.getCompany().getAddress());
        representativeService.addRepresentative(student.getCompany().getRepresentative());
        studentService.updateStudent(student);
        return "redirect:/students/studentProfile/" + student.getUserId();
    }

    @GetMapping("/addStudent")
    public String addStudentForm(Model model) {
        model.addAttribute("addStudentForm", new Student());
        return "forms/addStudentForm";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student) {
        student.setRole("student".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        String email = student.getEmail();
        addressService.addAddress(student.getAddress());
        studentService.addStudent(student);
        Student studentAfterCreation = studentService.getStudentByEmail(email);
        List<Subject> studentsSubjectList = subjectService.getAllSubjectsByFieldOfStudy(studentAfterCreation.getFieldOfStudy());
        for (Subject subject : studentsSubjectList) {
            studentAfterCreation.addGrade(gradeService.addGrade(new Grade(studentAfterCreation, subject, 0)));
        }
        return "redirect:/students/list";
    }

    @GetMapping("/delete")
    public String deleteStudentById(@RequestParam("userId") long userId) {
        internService.deleteInternById(studentService.getStudentById(userId).getIntern().getInternId());
        studentService.deleteStudentById(userId);

        return "redirect:/students/list";
    }

    @GetMapping("/edit/{userId}")
    public String editStudentForm(@PathVariable(value = "userId") int userId, Model model) {
        Student student = studentService.getStudentById(userId);
        model.addAttribute("editStudentForm", student);
        return "forms/editStudentForm";
    }

    @PostMapping("/edit/{userId}")
    public String editStudent(@ModelAttribute Student student) {
        Student student1 = studentService.getStudentById(student.getUserId());
        student.setRole("student".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        student.setCompany(student1.getCompany());
        if (!student.getFieldOfStudy().equals(student1.getFieldOfStudy())) {
            List<Grade> gradeList = gradeService.getStudentsGrades(student.getUserId());
            for (Grade grade : gradeList) {
                gradeService.deleteGradeById(grade.getGradeId());
            }
            List<Subject> studentsSubjectList = subjectService.getAllSubjectsByFieldOfStudy(student.getFieldOfStudy());
            for (Subject subject : studentsSubjectList) {
                student.addGrade(gradeService.addGrade(new Grade(student, subject, 0)));
            }
        }
        addressService.updateAddress(student.getAddress());
        studentService.updateStudent(student);
        return "redirect:/students/list";
    }

    //Student trainee journal

    @GetMapping("/traineeJournal/{userId}")
    public String getTraineeJournalByStudentId(@PathVariable(name = "userId") long studentId, Model model) {
        model.addAttribute("traineeJournal", traineeJournalService.getAllEntriesOfStudent(studentId));
        return "lists/traineeJournal";
    }

    @GetMapping("/addEntry/{userId}")
    public String addEntryForm(Model model, @PathVariable(name = "userId") long studentId) {
        Student student = studentService.getStudentById(studentId);
        TraineeJournal traineeJournal = new TraineeJournal();
        traineeJournal.setStudent(student);
        model.addAttribute("addNewEntryToTraineeJournalForm", traineeJournal);
        return "forms/addNewEntryToTraineeJournalForm";
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
        return "forms/editStudentEntryForm";
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
        return "documents/studentAgreementForm";
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
