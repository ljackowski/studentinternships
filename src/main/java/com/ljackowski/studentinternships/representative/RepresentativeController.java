package com.ljackowski.studentinternships.representative;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/representatives")
public class RepresentativeController {

    private final RepresentativeService representativeService;

    public RepresentativeController(RepresentativeService representativeService) {
        this.representativeService = representativeService;
    }

    @RequestMapping("/list")
    public String getAllRepresentatives(Model model){
        model.addAttribute("representatives", representativeService.getRepresentatives());
        return "representativesList";
    }
}
