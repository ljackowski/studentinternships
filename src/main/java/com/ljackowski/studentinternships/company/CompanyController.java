package com.ljackowski.studentinternships.company;

import com.ljackowski.studentinternships.representative.RepresentativeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final RepresentativeService representativeService;

    public CompanyController(CompanyService companyService, RepresentativeService representativeService) {
        this.companyService = companyService;
        this.representativeService = representativeService;
    }

    @RequestMapping("/list")
    public String getAllCompanies(Model model){
        List<Company> companies =  companyService.companyList();
        for (Company company : companies){
            company.setRepresentative(representativeService.getRepresentativeByCompanyName(company.getCompanyName()));
            companyService.updateCompany(company);
        }
        model.addAttribute("companies", companies);
        return "companiesList";
    }

    @GetMapping("/addCompany")
    public String addCompanyForm(Model model){
        model.addAttribute("addCompanyForm", new Company());
        return "addCompanyForm";
    }

    @PostMapping("/addCompany")
    public String addCompany(@ModelAttribute Company company){
        company.setRepresentative(representativeService.getRepresentativeByCompanyName(company.getCompanyName()));
        companyService.addCompany(company);
        return "redirect:/companies/list";
    }

    @GetMapping("/delete")
    public String deleteCompanyById(@RequestParam("companyId") long companyId){

        companyService.deleteById(companyId);
        return "redirect:/companies/list";
    }

    @GetMapping("/edit/{companyId}")
    public String editCompanyForm(@PathVariable("companyId") long companyId, Model model){
        Company company = companyService.getCompanyById(companyId);
        model.addAttribute("editCompanyForm", company);
        return "editCompanyForm";
    }

    @PostMapping("/edit/{companyId}")
    public String editCompany(@ModelAttribute Company company){
        companyService.updateCompany(company);
        return "redirect:/companies/list";
    }

}
