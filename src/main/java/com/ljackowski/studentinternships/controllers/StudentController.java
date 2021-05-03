package com.ljackowski.studentinternships.controllers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Journal;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final CoordinatorService coordinatorService;
    private final TemplateEngine templateEngine;
    private final JournalService journalService;
    private final InternService internService;
    private final AddressService addressService;
    private final CompanyService companyService;
    private final RepresentativeService representativeService;
    private final GuardianService guardianService;
    private final ServletContext servletContext;

    @Autowired
    public StudentController(StudentService studentService, CoordinatorService coordinatorService,
                             TemplateEngine templateEngine, JournalService journalService,
                             InternService internService, AddressService addressService,
                             CompanyService companyService, RepresentativeService representativeService,
                             GuardianService guardianService, ServletContext servletContext) {
        this.studentService = studentService;
        this.coordinatorService = coordinatorService;
        this.templateEngine = templateEngine;
        this.journalService = journalService;
        this.internService = internService;
        this.addressService = addressService;
        this.companyService = companyService;
        this.representativeService = representativeService;
        this.guardianService = guardianService;
        this.servletContext = servletContext;
    }

    @GetMapping("/{studentId}")
    public String studentProfileForm(@PathVariable(name = "studentId") long userId, Model model) {
        Student student = studentService.getStudentById(userId);
        if (student.getRole().equals("ROLE_STUDENT")) {
            if (student.getCompany() == null) {
                model.addAttribute("student", student);
                return "profiles/studentProfileBeforeSettingCompany";
            } else {
                model.addAttribute("student", student);
                return "profiles/studentProfile";
            }
        } else if (student.getRole().equals("ROLE_INTERN")) {
            Intern intern = internService.getInternByStudent(student);
            return "redirect:/interns/intern/" + intern.getInternId();
        }
        return "";
    }

    @PostMapping("/{studentId}")
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
        guardianService.addGuardian(student.getCompany().getGuardian());
        studentService.updateStudent(student);
        return "redirect:/students/student/" + student.getUserId();
    }

    //Trainee journal

    @GetMapping("/journal/{userId}")
    public String getTraineeJournalByStudentId(@PathVariable(name = "userId") long userId, Model model) {
        model.addAttribute("journal", studentService.getStudentById(userId).getJournal());
        return "lists/traineeJournal";
    }

    @GetMapping("/addEntry/{userId}")
    public String addEntryForm(Model model, @PathVariable(name = "userId") long userId) {
        Journal journal = new Journal();
        journal.setStudent(studentService.getStudentById(userId));
        model.addAttribute("journal", journal);
        return "forms/addNewEntryToStudentJournalForm";
    }

    @PostMapping("/addEntry/{userId}")
    public String addEntry(@ModelAttribute Journal journal, @PathVariable(name = "userId") long userId) {
        journal.setStudent(studentService.getStudentById(userId));
        journalService.addEntry(journal);
        return "redirect:/students/traineeJournal/" + userId;
    }

    @GetMapping("/deleteEntry")
    public String deleteStudentEntryById(@RequestParam("entryId") long entryId) {
        long userId = journalService.getEntryById(entryId).getStudent().getUserId();
        journalService.deleteEntryById(entryId);
        return "redirect:/students/traineeJournal/" + userId;
    }

    @GetMapping("/editEntry/{entryId}")
    public String editStudentEntryForm(@PathVariable("entryId") long entryId, Model model) {
        model.addAttribute("editStudentEntryForm", journalService.getEntryById(entryId));
        return "forms/editStudentEntryForm";
    }

    @PostMapping("/editEntry/{entryId}")
    public String editStudentEntry(@ModelAttribute Journal journal) {
        long userId = journalService.getEntryById(journal.getEntryId()).getStudent().getUserId();
        journalService.editEntry(journal);
        return "redirect:/students/traineeJournal/" + userId;
    }

    //PDFS

    @GetMapping("/trainingDeclaration/{studentId}")
    public ResponseEntity<?> generateTrainingDeclaration(@PathVariable("studentId") long studentId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateStudentPDF("documents/trainingDeclaration", studentService.getStudentById(studentId));
    }

    @GetMapping("/trainingAgreement/{studentId}")
    public ResponseEntity<?> generateTrainingAgreement(@PathVariable("studentId") long studentId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateStudentPDF("documents/trainingAgreement", studentService.getStudentById(studentId));
    }

    @GetMapping("/trainingJournal/{studentId}")
    public ResponseEntity<?> generateTrainingJournal(@PathVariable("studentId") long studentId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        Student student = studentService.getStudentById(studentId);
        student.setJournal(journalService.sortByDate(student.getJournal()));
        return pdfGeneration.generateStudentPDF("documents/traineeJournal", student);
    }
//
//    @RequestMapping("/pdf")
//    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, byteArrayOutputStream, converterProperties);
//        return pdfGeneration.generatePDF(request, response, "documents/trainingDeclaration");
//    }

}
