package org.example.ecommercestore_backend.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommercestore_backend.entity.User;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckOutDto {
    private UUID id;
    private String name;
    private String company;
    private String streetAddress;
    private String apartment;
    private String city;
    private String phone;
    private String email;
    private UUID userId;
    private UUID productId;
    private List<ImageDto> images;
    private Integer quantity;
}
