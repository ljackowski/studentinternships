package com.ljackowski.studentinternships.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    public Address getAddressById(long addressId){
        return addressRepository.findById(addressId).get();
    }

    public void addAddress(Address address){
        addressRepository.save(address);
    }

    public void deleteAddressById(long addressId){
        addressRepository.deleteById(addressId);
    }

    public void updateAddress(Address address){
        addressRepository.save(address);
    }
}
