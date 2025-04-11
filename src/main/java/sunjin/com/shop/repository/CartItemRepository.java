package sunjin.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunjin.com.shop.domain.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserId(int userId);
}
