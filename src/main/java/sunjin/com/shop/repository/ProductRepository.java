package sunjin.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunjin.com.shop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
