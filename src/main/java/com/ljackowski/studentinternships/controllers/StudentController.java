package com.ljackowski.studentinternships.controllers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.ljackowski.studentinternships.files.PDFGeneration;
import com.ljackowski.studentinternships.models.Journal;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final CoordinatorService coordinatorService;
    private final TemplateEngine templateEngine;
    private final JournalService journalService;
    private final AddressService addressService;
    private final CompanyService companyService;
    private final RepresentativeService representativeService;
    private final GuardianService guardianService;
    private final ServletContext servletContext;
    private final GradeService gradeService;

    @Autowired
    public StudentController(StudentService studentService, CoordinatorService coordinatorService,
                             TemplateEngine templateEngine, JournalService journalService,
                             AddressService addressService,
                             CompanyService companyService, RepresentativeService representativeService,
                             GuardianService guardianService, ServletContext servletContext, GradeService gradeService) {
        this.studentService = studentService;
        this.coordinatorService = coordinatorService;
        this.templateEngine = templateEngine;
        this.journalService = journalService;
        this.addressService = addressService;
        this.companyService = companyService;
        this.representativeService = representativeService;
        this.guardianService = guardianService;
        this.servletContext = servletContext;
        this.gradeService = gradeService;
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId")
    public String studentProfileForm(@PathVariable(name = "studentId") long studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        if (student.getCompany() == null) {
            model.addAttribute("student", student);
            return "student/studentProfileBeforeSettingCompany";
        } else {
            model.addAttribute("student", student);
            return "student/studentProfile";
        }
    }

    //Trainee journal

    @GetMapping("/journal/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public String getTraineeJournalByStudentId(@PathVariable(name = "studentId") long studentId, Model model) {
        model.addAttribute("journal", studentService.getStudentById(studentId).getJournal());
        return "student/traineeJournal";
    }

    @GetMapping("/addEntry/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public String addEntryForm(Model model, @PathVariable(name = "studentId") long studentId) {
        Journal journal = new Journal();
        journal.setStudent(studentService.getStudentById(studentId));
        model.addAttribute("journal", journal);
        return "student/addNewEntryToStudentJournalForm";
    }

    @PostMapping("/addEntry/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public String addEntry(@ModelAttribute Journal journal, @PathVariable(name = "studentId") long studentId) {
        journal.setStudent(studentService.getStudentById(studentId));
        journalService.addEntry(journal);
        return "redirect:/student/journal/" + studentId;
    }

    @GetMapping("/{studentId}/deleteEntry")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public String deleteStudentEntryById(@RequestParam("entryId") long entryId, @PathVariable("studentId") long studentId) {
        long userId = journalService.getEntryById(entryId).getStudent().getUserId();
        journalService.deleteEntryById(entryId);
        return "redirect:/student/journal/" + userId;
    }

    @GetMapping("/{studentId}/editEntry/{entryId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public String editStudentEntryForm(@PathVariable("entryId") long entryId, @PathVariable("studentId") long studentId, Model model) {
        model.addAttribute("entryToEdit", journalService.getEntryById(entryId));
        return "student/editStudentEntryForm";
    }

    @PostMapping("/{studentId}/editEntry/{entryId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public String editStudentEntry(@ModelAttribute Journal journal, @PathVariable("studentId") long studentId) {
        journalService.editEntry(journal);
        return "redirect:/student/journal/" + journal.getStudent().getUserId();
    }

    //PDFS

    @GetMapping("/trainingDeclaration/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public ResponseEntity<?> generateTrainingDeclaration(@PathVariable("studentId") long studentId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateStudentPDF("documents/trainingDeclaration", studentService.getStudentById(studentId));
    }

    @GetMapping("/trainingAgreement/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public ResponseEntity<?> generateTrainingAgreement(@PathVariable("studentId") long studentId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateStudentPDF("documents/trainingAgreement", studentService.getStudentById(studentId));
    }

    @GetMapping("/trainingJournal/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public ResponseEntity<?> generateTrainingJournal(@PathVariable("studentId") long studentId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        Student student = studentService.getStudentById(studentId);
        student.setJournal(journalService.sortByDate(student.getJournal()));
        return pdfGeneration.generateStudentPDF("documents/traineeJournal", student);
    }

    @GetMapping("/trainingProgram/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public ResponseEntity<?> generateTrainingProgram(@PathVariable("studentId") long studentId) throws IOException {
        return new PDFGeneration(new HttpHeaders()).generateStudentPDF("Ramowy_Program_Praktyk_zawodowych_informatyka_2021.pdf");
    }

    @GetMapping("/trainingRules/{studentId}")
    @PreAuthorize("authentication.principal.userId == #studentId and authentication.principal.company != null")
    public ResponseEntity<?> generateTrainingRules(@PathVariable("studentId") long studentId) throws IOException {
        return new PDFGeneration(new HttpHeaders()).generateStudentPDF("Regulamin-studenckich-praktyk-zawodowych.pdf");
    }
//
//    @RequestMapping("/pdf")
//    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, byteArrayOutputStream, converterProperties);
//        return pdfGeneration.generatePDF(request, response, "documents/trainingDeclaration");
//    }

}
