package sunjin.com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.repository.CartItemRepository;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    public CartItem addToCart(int userId, int productId, int quantity) {
        // 상품 존재 여부 확인
        Product product = productService.getProductById(productId);
        if(product == null) {
            throw new IllegalArgumentException("해당 상품을 찾을 수 없습니다.");
        }

        // 재고 확인
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItemsByUserId(int userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
