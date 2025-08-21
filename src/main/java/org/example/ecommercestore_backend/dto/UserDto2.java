package org.example.ecommercestore_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto2 {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private List<String> roles;
    private List<WishListDto> wishList;
    private List<RatingDto> ratings;
    private List<CartDto> cart;
}
