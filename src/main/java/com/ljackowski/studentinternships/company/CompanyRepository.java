package com.ljackowski.studentinternships.company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByCompanyName(String companyName);
}
