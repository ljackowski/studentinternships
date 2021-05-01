package com.ljackowski.studentinternships.repositories;

import com.ljackowski.studentinternships.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
