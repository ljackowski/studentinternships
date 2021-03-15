package com.ljackowski.studentinternships.representative;

import com.ljackowski.studentinternships.company.CompanyService;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/representatives")
public class RepresentativeController {

    private final RepresentativeService representativeService;
    private final CompanyService companyService;

    public RepresentativeController(RepresentativeService representativeService, CompanyService companyService) {
        this.representativeService = representativeService;
        this.companyService = companyService;
    }

    @RequestMapping("/list")
    public String getAllRepresentatives(Model model){
        model.addAttribute("representatives", representativeService.getRepresentatives());
        return "representativesList";
    }

    @RequestMapping("/representative/{representativeId}")
    @ResponseBody
    public Representative getRepresentative(@PathVariable("representativeId") long representativeId){
        return representativeService.getRepresentativeById(representativeId);
    }

    @GetMapping("/addRepresentative")
    public String addRepresentativeForm(Model model){
        model.addAttribute("addRepresentativeForm", new Representative());
        return "addRepresentativeForm";
    }

    @PostMapping("/addRepresentative")
    public String addRepresentative(@ModelAttribute Representative representative){
        representative.setCompany(companyService.getComanyByCompanyName(representative.getCompanyName()));
        representativeService.addRepresentative(representative);
        return "redirect:/representatives/list";
    }

    @GetMapping("/delete")
    public String deleteRepresentative(@RequestParam long representativeId){
        representativeService.deleteRepresentative(representativeId);
        return "redirect:/representatives/list";
    }

    @GetMapping("/edit/{representativeId}")
    public String editRepresentativeForm(@PathVariable("representativeId") long representativeId, Model model){
        Representative representative = representativeService.getRepresentativeById(representativeId);
        model.addAttribute("editRepresentativeForm", representative);
        return "editRepresentativeForm";
    }

    @PostMapping("/edit/{representativeId}")
    public String editRepresentative(@ModelAttribute Representative representative){
        representativeService.updateRepresentative(representative);
        return "redirect:/representatives/list";
    }



}
