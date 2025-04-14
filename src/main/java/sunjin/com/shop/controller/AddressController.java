package sunjin.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.Address;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.dto.AddAddressRequest;
import sunjin.com.shop.service.AddressService;
import sunjin.com.shop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Address> addAddress(
            @AuthenticationPrincipal UserDetails userDetails, AddAddressRequest request) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Address address = addressService.addAddress(getUserId(userDetails), request);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Address>> getAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        List<Address> addresses = addressService.getAddressesByUserId(getUserId(userDetails));
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int addressId) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Address address = addressService.getAddressById(addressId);
        if (address == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(address);
    }

    private int getUserId(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다: " + userDetails.getUsername());
        }
        return user.getUserId();
    }
}