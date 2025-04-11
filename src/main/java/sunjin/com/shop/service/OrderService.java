package sunjin.com.shop.service;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.domain.Order;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AddressService addressService;

    @Transactional
    public Order createOrder(int userId, int addressId) {
        if (addressService.getAddressById(addressid) == null) {
            throw new IllegalArgumentException("주소를 찾을 수 없습니다.");
        }

        List<CartItem> cartItems = cartItemService.getCartItemsByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        int totalAmount = 0;
        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + cartItem.getProductId());
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("재고가 부족합니다: " + product.getName());
            }
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.updateProduct(product);
            cartItemService.deleteCartItem(cartItem.getCartItemId());
        }

        return order;
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }
}
