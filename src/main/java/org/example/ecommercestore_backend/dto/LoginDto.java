package org.example.ecommercestore_backend.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String name;
    private String email;
    private String password;
}

