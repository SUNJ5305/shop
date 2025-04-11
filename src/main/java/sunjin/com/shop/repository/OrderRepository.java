package sunjin.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunjin.com.shop.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
}
