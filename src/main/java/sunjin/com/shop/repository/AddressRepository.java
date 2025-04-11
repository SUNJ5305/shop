package sunjin.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunjin.com.shop.domain.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUserId(int userId);
}