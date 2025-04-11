package sunjin.com.shop.service;

import sunjin.com.shop.domain.Address;
import sunjin.com.shop.domain.CartItem;
import sunjin.com.shop.domain.Order;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.domain.User; // 추가
import sunjin.com.shop.repository.AddressRepository;
import sunjin.com.shop.repository.CartItemRepository;
import sunjin.com.shop.repository.OrderRepository;
import sunjin.com.shop.repository.ProductRepository;
import sunjin.com.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        cartItemRepository.deleteAll();
        productRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateOrder() {
        // 유저 등록
        User user = userService.createUser("test@test.com", "1234", "John"); // User 객체 반환
        // 상품 등록
        Product product = productService.createProduct("iPhone", "Latest model", 1000000, 10, null);
        // 주소 등록
        Address address = addressService.addAddress(user.getUserId(), "서울시 강남구", "서울", "12345", true); // user.getUserId() 사용
        // 장바구니 추가
        cartItemService.addToCart(user.getUserId(), product.getProductId(), 2);

        // 주문 생성
        Order order = orderService.createOrder(user.getUserId(), address.getAddressId());
        assertNotNull(order);
        assertEquals(2000000, order.getTotalAmount());

        // 재고 감소 확인
        Product updatedProduct = productService.getProductById(product.getProductId());
        assertEquals(8, updatedProduct.getStock());

        // 장바구니 비워졌는지 확인
        List<CartItem> cartItems = cartItemService.getCartItemsByUserId(user.getUserId());
        assertTrue(cartItems.isEmpty());
    }

    @Test
    void testCreateOrderWithEmptyCart() {
        User user = userService.createUser("test@test.com", "1234", "John");
        Address address = addressService.addAddress(user.getUserId(), "서울시 강남구", "서울", "12345", true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(user.getUserId(), address.getAddressId());
        });
        assertEquals("장바구니가 비어 있습니다.", exception.getMessage());
    }

    @Test
    void testGetOrdersByUserId() {
        User user = userService.createUser("test@test.com", "1234", "John");
        Product product = productService.createProduct("iPhone", "Latest model", 1000000, 10, null);
        Address address = addressService.addAddress(user.getUserId(), "서울시 강남구", "서울", "12345", true);
        cartItemService.addToCart(user.getUserId(), product.getProductId(), 2);

        Order order = orderService.createOrder(user.getUserId(), address.getAddressId());
        List<Order> orders = orderService.getOrdersByUserId(user.getUserId());
        assertEquals(1, orders.size());
        assertEquals(2000000, orders.get(0).getTotalAmount());
    }
}