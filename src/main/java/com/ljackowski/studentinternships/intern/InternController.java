package com.ljackowski.studentinternships.intern;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.ljackowski.studentinternships.company.Company;
import com.ljackowski.studentinternships.company.CompanyService;
import com.ljackowski.studentinternships.documentsgeneration.PDFGeneration;
import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import com.ljackowski.studentinternships.journal.Journal;
import com.ljackowski.studentinternships.journal.JournalService;
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
    private final JavaMailSender javaMailSender;
    private final ServletContext servletContext;
    private final TemplateEngine templateEngine;
    private final JournalService journalService;

    @Autowired
    public InternController(InternService internService, CompanyService companyService, StudentService studentService, JavaMailSender javaMailSender, ServletContext servletContext, TemplateEngine templateEngine, JournalService journalService) {
        this.internService = internService;
        this.companyService = companyService;
        this.studentService = studentService;
        this.javaMailSender = javaMailSender;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.journalService = journalService;
    }

    public void SendInternshipNotification(Intern intern) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(intern.getStudent().getEmail());
        mailMessage.setFrom("pexy3475@gmail.com");
        mailMessage.setSubject("Informacja o stażu");
        if (!intern.isReserve()) {
            mailMessage.setText("Gratulujemy dostał się pan na staż na listę główną, proszę zalogować się do SOPiS przy pomocy tego emaila: "
                    + intern.getStudent().getEmail()
                    + "\n oraz tego hasła: "
                    + intern.getStudent().getPassword());
        } else {
            mailMessage.setText("Gratulujemy dostał się pan na staż na listę rezerwową. Poinformujemy Pana/Panią jeśli miejsce na liście głównej się zwolni");
        }
        javaMailSender.send(mailMessage);
    }

    @RequestMapping("/list")
    public String getAllStudents(Model model) {
        List<Intern> interns = internService.getAllInterns();
        model.addAttribute("interns", interns);
        return "lists/internsList";
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


    @GetMapping("/addInterns")
    public String populateInternsTable() {
        List<Intern> internList = internService.getAllInterns();
        if (internList.isEmpty()) {
            int i = 1;
            List<Student> studentsQualified = studentService.getFirst20StudentsByAvgGrade();
            for (Student student : studentsQualified) {
                student.setRole("INTERN");
                if (i <= 2) {
                    internService.addIntern(new Intern(student, null, false));
                } else {
                    internService.addIntern(new Intern(student, null, true));
                }
                i++;
            }
//            List<Intern> interns = internService.getAllInterns();
//            for (Intern intern : interns) {
//                SendInternshipNotification(intern);
//            }
        }
        return "redirect:/interns/list";
    }

    @GetMapping("/delete")
    public String deleteInternById(@RequestParam("internId") long internId) {
        Intern intern = internService.getInternById(internId);
        if (!intern.isReserve()) {
            List<Intern> interns = internService.getAllInterns();
            Intern intern1 = internService.getInternById(interns.get(interns.indexOf(intern) + 1).getInternId());
            intern1.setReserve(false);
            SendInternshipNotification(intern1);
            internService.updateIntern(intern1);
        }
        internService.deleteInternById(internId);
        return "redirect:/interns/list";
    }

    @GetMapping("/deleteAllInterns")
    public String deleteAllInterns() {
        List<Intern> interns = internService.getAllInterns();
        for (Intern intern : interns) {
            intern.getStudent().setRole("STUDENT");
        }
        internService.deleteAll();
        return "redirect:/interns/list";
    }

//    Internship Journal

    @GetMapping("/internJournal/{internId}")
    public String getInternJournalByInternId(@PathVariable(name = "internId") long internId, Model model) {
        model.addAttribute("journal", journalService.getAllEntriesOfStudent(internService.getInternById(internId).getStudent().getUserId()));
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
    public ResponseEntity<?> generateInternshipBill(@PathVariable("internId") long internId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateInternPDF("documents/internshipBill", internService.getInternById(internId));
    }

    @GetMapping("/internshipJournal/{internId}")
    public ResponseEntity<?> generateInternshipJournal(@PathVariable("internId") long internId, HttpServletRequest request, HttpServletResponse response) {
        PDFGeneration pdfGeneration = new PDFGeneration(templateEngine, servletContext, new ByteArrayOutputStream(), new ConverterProperties(), request, response);
        return pdfGeneration.generateInternPDF("documents/journal", internService.getInternById(internId));
    }

}
