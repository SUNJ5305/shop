package sunjin.com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAddressRequest {
    String addressLine;
    String city;
    String postalCode;
    boolean isDefault;
}
