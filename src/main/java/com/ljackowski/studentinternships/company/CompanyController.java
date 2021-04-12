package com.ljackowski.studentinternships.company;

import com.ljackowski.studentinternships.address.AddressService;
import com.ljackowski.studentinternships.representative.RepresentativeService;
import com.ljackowski.studentinternships.student.Student;
import com.ljackowski.studentinternships.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final RepresentativeService representativeService;
    private final AddressService addressService;
    private final StudentService studentService;

    @Autowired
    public CompanyController(CompanyService companyService, RepresentativeService representativeService, AddressService addressService, StudentService studentService) {
        this.companyService = companyService;
        this.representativeService = representativeService;
        this.addressService = addressService;
        this.studentService = studentService;
    }

    @RequestMapping("/list")
    public String getAllCompanies(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        for (Company company : companies) {
            companyService.updateCompany(company);
        }
        model.addAttribute("companies", companies);
        return "lists/companiesList";
    }

    @RequestMapping("/company/{companyId}")
    public String getCompanyById(Model model, @PathVariable("companyId")long companyId){
        Company company = companyService.getCompanyById(companyId);
        if (company.isPartOfInternship()){
            model.addAttribute("company", company);
            return "profiles/companyInternshipProfile";
        }
        else {
            model.addAttribute("company", company);
            return "profiles/companyStudentProfile";
        }
    }

    @GetMapping("/addCompanyToInternShip")
    public String addCompanyForm(Model model) {
        model.addAttribute("addCompanyForm", new Company());
        return "forms/addCompanyToInternshipForm";
    }

    @PostMapping("/addCompanyToInternShip")
    public String addCompanyToInternShip(@ModelAttribute Company company) {
        company.setPartOfInternship(true);
        representativeService.addRepresentative(company.getRepresentative());
        addressService.addAddress(company.getAddress());
        companyService.addCompany(company);
        return "redirect:/companies/list";
    }

    @GetMapping("/delete")
    public String deleteCompanyById(@RequestParam("companyId") long companyId) {
        for (Student student : studentService.getAllStudentsInCompany(companyService.getCompanyById(companyId))) {
            student.setCompany(null);
        }
        companyService.deleteById(companyId);
        return "redirect:/companies/list";
    }

    @GetMapping("/edit/{companyId}")
    public String editCompanyForm(@PathVariable("companyId") long companyId, Model model) {
        Company company = companyService.getCompanyById(companyId);
        model.addAttribute("editCompanyForm", company);
        return "forms/editCompanyForm";
    }

    @PostMapping("/edit/{companyId}")
    public String editCompany(@ModelAttribute Company company) {
        addressService.updateAddress(company.getAddress());
        representativeService.updateRepresentative(company.getRepresentative());
        companyService.updateCompany(company);
        return "redirect:/companies/list";
    }

}
