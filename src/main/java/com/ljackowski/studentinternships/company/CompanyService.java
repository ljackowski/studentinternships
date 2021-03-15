package com.ljackowski.studentinternships.company;

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

    public List<Company> companyList(){
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

    public Company getComanyByCompanyName(String companyName){
        return companyRepository.findByCompanyName(companyName);
    }
}
