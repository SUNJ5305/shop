package sunjin.com.shop.service;

import sunjin.com.shop.domain.Address;
import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.domain.Order;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.repository.AddressRepository;
import sunjin.com.shop.repository.CartItemRepository;
import sunjin.com.shop.repository.OrderRepository;
import sunjin.com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Order createOrder(int userId, int addressId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        Address address = addressRepository.findById(addressId).orElse(null);
        if (address == null) {
            throw new IllegalArgumentException("주소를 찾을 수 없습니다: " + addressId);
        }

        int totalAmount = 0;
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            if (product == null) {
                throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + cartItem.getProductId());
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("재고가 부족합니다: " + product.getName());
            }
            totalAmount += product.getPrice() * cartItem.getQuantity();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setOrderedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }
}