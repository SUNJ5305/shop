package sunjin.com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.Address;
import sunjin.com.shop.repository.AddressRepository;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address getAddressById(int addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }
}