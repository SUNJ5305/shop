package sunjin.com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.Address;
import sunjin.com.shop.dto.AddAddressRequest;
import sunjin.com.shop.repository.AddressRepository;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address addAddress(int userId, AddAddressRequest request) {
        Address address = new Address();
        address.setUserId(userId);
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setDefaultAddress(request.isDefault());
        return addressRepository.save(address);
    }

    public Address getAddressById(int addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    public List<Address> getAddressesByUserId(int userId) {
        return addressRepository.findByUserId(userId);
    }
}