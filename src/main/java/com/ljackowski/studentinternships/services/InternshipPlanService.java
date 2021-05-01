package com.ljackowski.studentinternships.services;

import com.ljackowski.studentinternships.models.Intern;
import com.ljackowski.studentinternships.models.InternshipPlan;
import com.ljackowski.studentinternships.repositories.InternshipPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipPlanService {

    private final InternshipPlanRepository internshipPlanRepository;

    @Autowired
    public InternshipPlanService(InternshipPlanRepository internshipPlanRepository) {
        this.internshipPlanRepository = internshipPlanRepository;
    }

    public List<InternshipPlan> getInternPlan(Intern intern){
        return internshipPlanRepository.findAllByIntern(intern);
    }

    public InternshipPlan getPlanEntryById(long entryId){
        return internshipPlanRepository.findById(entryId).get();
    }

    public void addPlanEntry(InternshipPlan internshipPlan){
        internshipPlanRepository.save(internshipPlan);
    }

    public void deleteEntryById(long entryId){
        internshipPlanRepository.deleteById(entryId);
    }

    public void updateEntry(InternshipPlan internshipPlan){
        internshipPlanRepository.save(internshipPlan);
    }
}
