package sunjin.com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.Address;
import sunjin.com.shop.repository.AddressRepository;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address addAddress(int userId, String addressLine, String city, String postalCode, boolean isDefault) {
        Address address = new Address();
        address.setUserId(userId);
        address.setAddressLine(addressLine);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setDefaultAddress(isDefault);
        return addressRepository.save(address);
    }

    public Address getAddressById(int addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    public List<Address> getAddressesByUserId(int userId) {
        return addressRepository.findByUserId(userId);
    }
}