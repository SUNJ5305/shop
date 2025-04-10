package sunjin.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunjin.com.shop.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
