package org.example.ecommercestore_backend.service;
import org.example.ecommercestore_backend.dto.ProductDto;
import org.example.ecommercestore_backend.dto.RatingDto;
import org.example.ecommercestore_backend.entity.Category;
import org.example.ecommercestore_backend.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {
    List<ProductDto> findAll();
    Product save(String title, Integer price,String description, String color, String size, Integer quantity, Integer discount, Category category,
                 List<MultipartFile> images);
    void delete(UUID id);
    Product update(UUID id, String title, Integer price, String description, String color, String size, Integer quantity, Integer discount, Category category,
                   List<MultipartFile> images);

    ResponseEntity<?> addRating(UUID productId, UUID userId, Integer stars);

    ResponseEntity<List<RatingDto>> getRatings(UUID productId);
    ProductDto updateQuantity(UUID productId, Integer newQuantity);
}
