package sunjin.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunjin.com.shop.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}