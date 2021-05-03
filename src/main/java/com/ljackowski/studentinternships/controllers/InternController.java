package com.ljackowski.studentinternships.controllers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.ljackowski.studentinternships.services.InternService;
import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.services.CompanyService;
import com.ljackowski.studentinternships.documentsgeneration.InternshipBill;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.Student;
import com.ljackowski.studentinternships.services.StudentService;
import com.ljackowski.studentinternships.models.Journal;
import com.ljackowski.studentinternships.services.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/interns")
public class InternController {

    private final InternService internService;
    private final CompanyService companyService;
    private final StudentService studentService;
    private final ServletContext servletContext;
    private final TemplateEngine templateEngine;
    private final JournalService journalService;

    @Autowired
    public InternController(InternService internService, CompanyService companyService, StudentService studentService,
                            ServletContext servletContext, TemplateEngine templateEngine, JournalService journalService) {
        this.internService = internService;
        this.companyService = companyService;
        this.studentService = studentService;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.journalService = journalService;
    }

    @GetMapping("/intern/{internId}")
    public String internProfileForm(Model model, @PathVariable(name = "internId") long internId) {
        Intern intern = internService.getInternById(internId);
        List<Company> companies = companyService.getCompaniesInInternship(true, 0);
        if (intern.getCompany() == null) {
            model.addAttribute("companies", companies);
            model.addAttribute("intern", intern);
            return "profiles/internProfileBeforeChoosingCompany";
        } else {
            model.addAttribute("intern", intern);
            return "profiles/internProfile";
        }
    }

    @PostMapping("/intern/{internId}")
    public String internProfile(@ModelAttribute Intern intern) {
        Company company = companyService.getCompanyById(intern.getCompany().getCompanyId());
        company.setFreeSpaces(company.getFreeSpaces() - 1);
        companyService.updateCompany(company);
        internService.updateIntern(intern);
        return "redirect:/interns/intern/" + intern.getInternId();
    }

//    Internship Journal

    @GetMapping("/journal/{internId}")
    public String getInternJournalByInternId(@PathVariable(name = "internId") long internId, Model model) {
        model.addAttribute("journal", internService.getInternById(internId).getStudent().getJournal());
        return "lists/internshipJournal";
    }

    @GetMapping("/addEntry/{internId}")
    public String addInternEntryForm(Model model, @PathVariable(name = "internId") long internId) {
        Journal journal = new Journal();
        journal.setStudent(studentService.getStudentById(internService.getInternById(internId).getStudent().getUserId()));
        model.addAttribute("journal", journal);
        return "forms/addNewEntryToInternshipJournalForm";
    }

    @PostMapping("/addEntry/{internId}")
    public String addInternEntry(@ModelAttribute Journal journal, @PathVariable(name = "internId") long internId) {
        journal.setStudent(studentService.getStudentById(internService.getInternById(internId).getStudent().getUserId()));
        journalService.addEntry(journal);
        return "redirect:/interns/internJournal/" + internId;
    }

    @GetMapping("/deleteEntry")
    public String deleteInternEntryById(@RequestParam("entryId") long entryId) {
        long internId = journalService.getEntryById(entryId).getStudent().getIntern().getInternId();
        journalService.deleteEntryById(entryId);
        return "redirect:/interns/internJournal/" + internId;
    }

    @GetMapping("/editEntry/{entryId}")
    public String editInternEntryForm(@PathVariable("entryId") long entryId, Model model) {
        model.addAttribute("journal", journalService.getEntryById(entryId));
        return "forms/editInternEntryForm";
    }

    @PostMapping("/editEntry/{entryId}")
    public String editInternEntry(@ModelAttribute Journal journal) {
        journal.setStudent(journalService.getEntryById(journal.getEntryId()).getStudent());
        journalService.editEntry(journal);
        return "redirect:/interns/internJournal/" + journal.getStudent().getIntern().getInternId();
    }

//    PDFS

    @GetMapping("/internshipProgram/{internId}")
    public ResponseEntity<?> generateInternshipProgram(@PathVariable("internId") long internId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateInternPDF("documents/internshipProgram", internService.getInternById(internId));
    }

    @GetMapping("/internshipBill/{internId}")
    public String generateInternshipBillForm(Model model, @PathVariable("internId") long internId) {
        model.addAttribute("internId", internId);
        model.addAttribute("bill", new InternshipBill());
        return "forms/internshipBillForm";
    }

    @PostMapping("/internshipBill/{internId}")
    public ResponseEntity<?> generateInternshipBill(@ModelAttribute InternshipBill internshipBill, HttpServletRequest request, HttpServletResponse response) {
        int sum = 0;
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        Intern intern = internService.getInternById(internshipBill.getInternId());
        List<Journal> journalList = intern.getStudent().getJournal();
        for (Journal journal : journalList) {
            sum += journal.getHours();
        }
        internshipBill.setHoursSum(sum);
        return pdfGeneration.generateInternPDF("documents/internshipBill", intern, internshipBill);
    }

    @GetMapping("/internshipJournal/{internId}")
    public ResponseEntity<?> generateInternshipJournal(@PathVariable("internId") long internId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        Intern intern = internService.getInternById(internId);
        intern.getStudent().setJournal(journalService.sortByDate(intern.getStudent().getJournal()));
        return pdfGeneration.generateInternPDF("documents/internshipJournal", intern);
    }

}
