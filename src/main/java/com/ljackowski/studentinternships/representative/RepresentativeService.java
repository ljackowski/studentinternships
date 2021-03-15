package com.ljackowski.studentinternships.representative;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepresentativeService {
    private final RepresentativeRepository representativeRepository;

    @Autowired
    public RepresentativeService(RepresentativeRepository representativeRepository) {
        this.representativeRepository = representativeRepository;
    }

    public List<Representative> getRepresentatives(){
        return representativeRepository.findAll();
    }

    public Representative getRepresentativeById(long representativeId){
        return representativeRepository.findById(representativeId).get();
    }

    public void addRepresentative(Representative representative){
        representativeRepository.save(representative);
    }

    public void deleteRepresentative(long employerId){
        representativeRepository.deleteById(employerId);
    }

    public void updateRepresentative(Representative representative){
        representativeRepository.save(representative);
    }

    public Representative getRepresentativeByCompanyName(String companyName){
        return representativeRepository.findByCompanyName(companyName);
    }
}
