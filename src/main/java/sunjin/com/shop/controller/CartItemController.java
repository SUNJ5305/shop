package sunjin.com.shop.controller;

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
    public ResponseEntity<CartItem> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int productId,
            @RequestParam int quantity) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        CartItem cartItem = cartItemService.addToCart(getUserId(userDetails), productId, quantity);
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CartItem>> getCartItems(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        List<CartItem> cartItems = cartItemService.getCartItemsByUserId(getUserId(userDetails));
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int cartItemId) {
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

    @PutMapping("/cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable int cartItemId, @RequestParam int quantity) {
        CartItem updatedCartItem = cartItemService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }

    private int getUserId(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다: " + userDetails.getUsername());
        }
        return user.getUserId();
    }
}