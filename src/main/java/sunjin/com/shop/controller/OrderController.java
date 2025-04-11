package sunjin.com.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.Order;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.service.OrderService;
import sunjin.com.shop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "현재 로그인한 유저의 새로운 주문을 생성합니다. 주문 상태(status)는 0(주문됨)으로 설정됩니다.")
    @ApiResponse(responseCode = "200", description = "주문 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "주소 ID") @RequestParam int addressId) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Order order = orderService.createOrder(getUserId(userDetails), addressId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user")
    @Operation(summary = "유저의 주문 목록 조회", description = "현재 로그인한 유저의 주문 목록을 조회합니다. 주문 상태(status): 0=대기, 1=결제완료, 2=배송중, 3=배송완료")
    @ApiResponse(responseCode = "200", description = "주문 목록 반환")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<List<Order>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        List<Order> orders = orderService.getOrdersByUserId(getUserId(userDetails));
        return ResponseEntity.ok(orders);
    }

    private int getUserId(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다: " + userDetails.getUsername());
        }
        return user.getUserId();
    }
}