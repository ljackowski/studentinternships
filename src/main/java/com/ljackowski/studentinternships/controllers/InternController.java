package com.ljackowski.studentinternships.controllers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.InternshipBill;
import com.ljackowski.studentinternships.models.Journal;
import com.ljackowski.studentinternships.services.CompanyService;
import com.ljackowski.studentinternships.services.InternService;
import com.ljackowski.studentinternships.services.JournalService;
import com.ljackowski.studentinternships.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/intern")
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

    @GetMapping("/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId")
    public String internProfileForm(Model model, @PathVariable(name = "internId") long internId) {
        Intern intern = internService.getInternByStudent(studentService.getStudentById(internId));
        List<Company> companies = companyService.getCompaniesInInternship(true, 0);
        if (intern.getCompany() == null) {
            model.addAttribute("companies", companies);
            model.addAttribute("intern", intern);
            return "intern/internProfileBeforeChoosingCompany";
        } else {
            model.addAttribute("intern", intern);
            return "intern/internProfile";
        }
    }

    @PostMapping("/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId")
    public String internProfile(@ModelAttribute Intern intern, @PathVariable(name = "internId") long internId) {
        Company company = companyService.getCompanyById(intern.getCompany().getCompanyId());
        company.setFreeSpaces(company.getFreeSpaces() - 1);
        companyService.updateCompany(company);
        Authentication authentication = new UsernamePasswordAuthenticationToken(intern, intern.getStudent().getPassword(),
                Arrays.stream(intern.getStudent().getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        internService.updateIntern(intern);
        return "redirect:/interns/intern/" + intern.getInternId();
    }

//    Internship Journal

    @GetMapping("/journal/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String getInternJournalByInternId(@PathVariable(name = "internId") long internId, Model model) {
        model.addAttribute("journal", studentService.getStudentById(internId).getJournal());
        return "intern/internshipJournal";
    }

    @GetMapping("/addEntry/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String addInternEntryForm(Model model, @PathVariable(name = "internId") long internId) {
        Journal journal = new Journal();
        journal.setStudent(studentService.getStudentById(internId));
        model.addAttribute("journal", journal);
        return "intern/addNewEntryToInternshipJournalForm";
    }

    @PostMapping("/addEntry/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String addInternEntry(@ModelAttribute Journal journal, @PathVariable(name = "internId") long internId) {
        journal.setStudent(studentService.getStudentById(internId));
        journalService.addEntry(journal);
        return "redirect:/intern/journal/" + internId;
    }

    @GetMapping("/{internId}/deleteEntry")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String deleteInternEntryById(@PathVariable(name = "internId") long internId, @RequestParam("entryId") long entryId) {
        journalService.deleteEntryById(entryId);
        return "redirect:/intern/journal/" + internId;
    }

    @GetMapping("/{internId}/editEntry/{entryId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String editInternEntryForm(@PathVariable("entryId") long entryId, Model model, @PathVariable(name = "internId") long internId) {
        model.addAttribute("journal", journalService.getEntryById(entryId));
        return "intern/editInternEntryForm";
    }

    @PostMapping("/{internId}/editEntry/{entryId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String editInternEntry(@ModelAttribute Journal journal, @PathVariable(name = "internId") long internId) {
        journal.setStudent(journalService.getEntryById(journal.getEntryId()).getStudent());
        journalService.editEntry(journal);
        return "redirect:/intern/journal/" + journal.getStudent().getUserId();
    }

//    PDFS

    @GetMapping("/internshipProgram/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public ResponseEntity<?> generateInternshipProgram(@PathVariable("internId") long internId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateInternPDF("documents/internshipProgram", internService.getInternById(internId));
    }

    @GetMapping("/internshipBill/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public String generateInternshipBillForm(Model model, @PathVariable("internId") long internId) {
        model.addAttribute("internId", internId);
        model.addAttribute("bill", new InternshipBill());
        return "intern/internshipBillForm";
    }

    @PostMapping("/internshipBill/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public ResponseEntity<?> generateInternshipBill(@PathVariable("internId") long internId, @ModelAttribute InternshipBill internshipBill, HttpServletRequest request, HttpServletResponse response) {
        int sum = 0;
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        Intern intern = internService.getInternByStudent(studentService.getStudentById(internId));
        List<Journal> journalList = intern.getStudent().getJournal();
        for (Journal journal : journalList) {
            sum += journal.getHours();
        }
        internshipBill.setHoursSum(sum);
        return pdfGeneration.generateInternPDF("documents/internshipBill", intern, internshipBill);
    }

    @GetMapping("/internshipJournal/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public ResponseEntity<?> generateInternshipJournal(@PathVariable("internId") long internId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        Intern intern = internService.getInternById(internId);
        intern.getStudent().setJournal(journalService.sortByDate(intern.getStudent().getJournal()));
        return pdfGeneration.generateInternPDF("documents/internshipJournal", intern);
    }

    @GetMapping("/declaration/{internId}")
    @PreAuthorize("authentication.principal.userId == #internId and authentication.principal.company != null")
    public ResponseEntity<?> generateDeclaration(@PathVariable("internId") long internId) throws IOException {
        return new PDFGeneration(new HttpHeaders()).generateInternPDF("Deklaracja_oświadczenie.pdf");
    }

}
