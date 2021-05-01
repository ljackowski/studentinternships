package com.ljackowski.studentinternships.services;

import com.ljackowski.studentinternships.models.Company;
import com.ljackowski.studentinternships.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long companyId){
        return companyRepository.findById(companyId).get();
    }

    public void addCompany(Company company){
        companyRepository.save(company);
    }

    public void updateCompany(Company company){
        companyRepository.save(company);
    }

    public void deleteById(long companyId){
        companyRepository.deleteById(companyId);
    }

    public List<Company> getCompaniesInInternship(boolean partOfInternship, int freeSpaces){
        return companyRepository.findAllByPartOfInternshipAndFreeSpacesGreaterThan(partOfInternship, freeSpaces);
    }
}
