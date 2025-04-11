package sunjin.com.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.Address;
import sunjin.com.shop.domain.User;
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
    @Operation(summary = "주소 추가", description = "현재 로그인한 유저의 새로운 주소를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "주소 추가 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<Address> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "주소") @RequestParam String addressLine,
            @Parameter(description = "도시") @RequestParam String city,
            @Parameter(description = "우편번호") @RequestParam String postalCode,
            @Parameter(description = "기본 주소 여부") @RequestParam boolean defaultAddress) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Address address = addressService.addAddress(getUserId(userDetails), addressLine, city, postalCode, defaultAddress);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/user")
    @Operation(summary = "유저의 주소 목록 조회", description = "현재 로그인한 유저의 주소 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주소 목록 반환")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<List<Address>> getAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        List<Address> addresses = addressService.getAddressesByUserId(getUserId(userDetails));
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "주소 조회", description = "ID로 주소를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주소 정보 반환")
    @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<Address> getAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "주소 ID") @PathVariable int addressId) {
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