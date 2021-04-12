package com.ljackowski.studentinternships.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByPartOfInternshipAndFreeSpacesGreaterThan(boolean partOfInternship, int freeSpaces);
}
