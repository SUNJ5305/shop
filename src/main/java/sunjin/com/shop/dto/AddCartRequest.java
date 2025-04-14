package sunjin.com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartRequest {
    int productId;
    int quantity;
}
