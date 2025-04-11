package sunjin.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItem> addToCart(
            @RequestParam int userId,
            @RequestParam int productId,
            @RequestParam int quantity) {
        CartItem cartItem = cartItemService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable int userId) {
        List<CartItem> cartItems = cartItemService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }
}
