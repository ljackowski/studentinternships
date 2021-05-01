package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByPartOfInternshipAndFreeSpacesGreaterThan(boolean partOfInternship, int freeSpaces);
}
