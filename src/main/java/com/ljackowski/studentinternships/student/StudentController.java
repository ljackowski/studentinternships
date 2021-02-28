package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.documentsgeneration.OrganizationAgreement;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
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
public class StudentController {
    private final StudentService studentService;
    private final TemplateEngine templateEngine;
    @Autowired
    ServletContext servletContext;

    @Autowired
    public StudentController(StudentService studentService, TemplateEngine templateEngine) {
        this.studentService = studentService;
        this.templateEngine = templateEngine;
    }

    @RequestMapping(path = "/studentProfile")
    public String studentProfile(Model model){
        model.addAttribute("studentProfile", model);
        return "studentProfile";
    }

    @PostMapping
    public void addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
    }

    @RequestMapping("studentsList")
    public String getAllStudents(Model model) {
        List<Student> studentsList = studentService.getAllStudents();
        model.addAttribute("students", studentsList);
        return "students";
    }

    @GetMapping(path = "student/{studentIndex}")
    public Student getStudentByIndex(@PathVariable("studentIndex") int studentIndex) {
        return studentService.getStudentByIndex(studentIndex).orElse(null);
    }

    @DeleteMapping(path = "delete/{nrIndeksu}")
    public void deleteStudentById(@PathVariable("studentId") int nrIndeksu) {
        studentService.deleteStudent(nrIndeksu);
    }

    @PutMapping(path = "edit/{nrIndeksu}")
    public void updateStudent(@PathVariable("nrIndeksu") int nrIndeksu, @RequestBody Student studentToUpdate) {
        studentService.updateStudent(nrIndeksu, studentToUpdate);
    }

    @GetMapping("organization")
    public String organizationForm(Model model){
        model.addAttribute("studentAgreementForm", new OrganizationAgreement());
        return "studentAgreementForm";
    }

    @PostMapping(path = "organization")
    public ResponseEntity<?> getPDF(@ModelAttribute OrganizationAgreement organizationAgreement, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext);
        return pdfGeneration.generateQuestionnaire(request, response, "Umowa_o_organizacje_praktyki_zawodowej", organizationAgreement);
    }

    @RequestMapping(path = "pdf")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext);
        return pdfGeneration.generatePDF(request, response, "Deklaracja_planowanej_praktyki_zawodowej");
    }

}
