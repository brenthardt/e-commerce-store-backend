package org.example.ecommercestore_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommercestore_backend.entity.Category;
import org.example.ecommercestore_backend.entity.Rating;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private UUID id;
    private String title;
    private Integer price;
    private String description;
    private String color;
    private String size;
    private Integer quantity;
    private Integer discount;
    private Category category;
   private Double averageStars;
    private List<RatingDto> ratings;
    private List<ImageDto> images;
}

