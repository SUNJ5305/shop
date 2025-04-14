package sunjin.com.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 주문 엔티티
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    /**
     * 주문 상태 열거형
     */
    public enum OrderStatus {
        PENDING(0),    // 대기
        PAID(1),       // 결제완료
        SHIPPING(2),   // 배송중
        DELIVERED(3);  // 배송완료

        private final int value;

        OrderStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static OrderStatus fromValue(int value) {
            for (OrderStatus status : values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("유효하지 않은 주문 상태: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "address_id", nullable = false)
    private int addressId;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private int status;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @PrePersist
    protected void onCreate() {
        this.orderedAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING.getValue();
    }
}