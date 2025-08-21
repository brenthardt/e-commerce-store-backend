package org.example.ecommercestore_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.ecommercestore_backend.entity.Rating;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String role;
    private String token;
    private String refreshToken;

}

