package sunjin.com.shop.service;

import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.dto.AddCartRequest;
import sunjin.com.shop.repository.CartItemRepository;
import sunjin.com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItem addToCart(int userId, AddCartRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + request.getProductId());
        }
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItem.setAddedAt(LocalDateTime.now());
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItemsByUserId(int userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void deleteCartItem(int cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public CartItem updateCartItem(int cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }
}