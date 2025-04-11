package sunjin.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.Address;
import sunjin.com.shop.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> addAddress(
            @RequestParam int userId,
            @RequestParam String addressLine,
            @RequestParam String city,
            @RequestParam String postalCode,
            @RequestParam boolean defaultAddress) {
        Address address = addressService.addAddress(userId, addressLine, city, postalCode, defaultAddress);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Address>> getAddresses(@PathVariable int userId) {
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }
}