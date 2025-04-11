package sunjin.com.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.domain.User;
import sunjin.com.shop.service.CartItemService;
import sunjin.com.shop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService; // 추가

    @PostMapping
    @Operation(summary = "장바구니에 상품 추가", description = "현재 로그인한 유저의 장바구니에 상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니 추가 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<CartItem> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "상품 ID") @RequestParam int productId,
            @Parameter(description = "수량") @RequestParam int quantity) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        CartItem cartItem = cartItemService.addToCart(getUserId(userDetails), productId, quantity);
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/user")
    @Operation(summary = "유저의 장바구니 목록 조회", description = "현재 로그인한 유저의 장바구니 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니 목록 반환")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    public ResponseEntity<List<CartItem>> getCartItems(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        List<CartItem> cartItems = cartItemService.getCartItemsByUserId(getUserId(userDetails));
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "장바구니 항목 삭제", description = "장바구니 항목을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 유저")
    @ApiResponse(responseCode = "404", description = "장바구니 항목을 찾을 수 없음")
    public ResponseEntity<Void> deleteCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "장바구니 항목 ID") @PathVariable int cartItemId) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            cartItemService.deleteCartItem(cartItemId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }


    private int getUserId(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다: " + userDetails.getUsername());
        }
        return user.getUserId();
    }
}