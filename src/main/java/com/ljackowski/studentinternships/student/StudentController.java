package com.ljackowski.studentinternships.student;

import com.ljackowski.studentinternships.coordinator.CoordinatorService;
import com.ljackowski.studentinternships.documentsgeneration.OrganizationAgreement;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.sun.istack.Nullable;
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
import java.util.Locale;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final CoordinatorService coordinatorService;
    private final TemplateEngine templateEngine;

    @Autowired
    ServletContext servletContext;

    @Autowired
    public StudentController(StudentService studentService, CoordinatorService coordinatorService, TemplateEngine templateEngine) {
        this.studentService = studentService;
        this.coordinatorService = coordinatorService;
        this.templateEngine = templateEngine;
    }

    @RequestMapping(path = "/studentProfile")
    public String studentProfile(Model model) {
        model.addAttribute("studentProfile", model);
        return "studentProfile";
    }

    @RequestMapping("/list")
    public String getAllStudents(Model model) {
        List<Student> studentsList = studentService.getStudents();
        for (Student student: studentsList){
            student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
            studentService.updateStudent(student);
        }
        model.addAttribute("students", studentsList);
        return "studentsList";
    }

    @RequestMapping("/student/{userId}")
    public Student getStudentByIndex(@PathVariable(name = "userId") long userId) {
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
        studentService.addStudent(student);
        return "redirect:/students/list";
    }

    @GetMapping(path = "/delete")
    public String deleteStudentById(@RequestParam("userId") long userId) {
        studentService.deleteStudentById(userId);
        return "redirect:/students/list";
    }

    @GetMapping("/edit/{userId}")
    public String editStudentForm(@PathVariable(value = "userId") int userId, Model model){
        Student student = studentService.getStudentById(userId);
        model.addAttribute("editStudentForm", student);
        return "editStudentForm";
    }

    @PostMapping("/edit/{userId}")
    public String editStudent(@ModelAttribute Student student){
        student.setRole("student".toUpperCase());
        student.setFieldOfStudy(student.getFieldOfStudy().toUpperCase());
        student.setCoordinator(coordinatorService.getCoordinatorByFieldOfStudy(student.getFieldOfStudy()));
        studentService.updateStudent(student);
        return "redirect:/students/list";
    }

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
