package sunjin.com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {
    private String name;
    private String description;
    private int price;
    private int stock;
    private Integer categoryId;
}
