package com.ljackowski.studentinternships.intern;

import com.ljackowski.studentinternships.company.Company;
import com.ljackowski.studentinternships.company.CompanyService;
import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/interns")
public class InternController {

    private final InternService internService;
    private final CompanyService companyService;
    private final StudentService studentService;
    private final JavaMailSender javaMailSender;

    @Autowired
    public InternController(InternService internService, CompanyService companyService, StudentService studentService, JavaMailSender javaMailSender) {
        this.internService = internService;
        this.companyService = companyService;
        this.studentService = studentService;
        this.javaMailSender = javaMailSender;
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
        for (Intern intern : interns){
            intern.getStudent().setRole("STUDENT");
        }
        internService.deleteAll();
        return "redirect:/interns/list";
    }


}
