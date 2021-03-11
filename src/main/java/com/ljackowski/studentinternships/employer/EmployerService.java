package com.ljackowski.studentinternships.employer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {
    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public List<Employer> getEmployers(){
        return employerRepository.findAll();
    }

    public Employer getEmployerById(long employerId){
        return employerRepository.findById(employerId).get();
    }

    public void addEmployer(Employer employer){
        employerRepository.save(employer);
    }

    public void deleteEmployerById(long employerId){
        employerRepository.deleteById(employerId);
    }

    public void updateEmployerById(Employer employer){
        employerRepository.save(employer);
    }
}
