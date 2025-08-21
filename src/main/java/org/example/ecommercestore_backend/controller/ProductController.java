package org.example.ecommercestore_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.ProductDto;
import org.example.ecommercestore_backend.dto.RatingDto;
import org.example.ecommercestore_backend.entity.Category;
import org.example.ecommercestore_backend.entity.Product;
import org.example.ecommercestore_backend.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductDto> create(
            @RequestParam String title,
            @RequestParam Integer price,
            @RequestParam String description,
            @RequestParam String color,
            @RequestParam String size,
            @RequestParam Integer quantity,
            @RequestParam Integer discount,
            @RequestParam String category,
            @RequestParam List<MultipartFile> images
    ) {
        Category categoryEnum = Category.valueOf(category.toUpperCase());
        productService.save(title, price,description,color,size, quantity
                , discount, categoryEnum, images);
        return productService.findAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductDto> update(
            @PathVariable UUID id,
            @RequestParam String title,
            @RequestParam Integer price,
            @RequestParam String description,
            @RequestParam String color,
            @RequestParam String size,
            @RequestParam Integer quantity,
            @RequestParam Integer discount,
            @RequestParam String category,
            @RequestParam(required = false) List<MultipartFile> images
    ) {
        Category categoryEnum = Category.valueOf(category.toUpperCase());
        productService.update(id, title, price, description,color,size, quantity
                , discount, categoryEnum, images);
        return productService.findAll();
    }

    @DeleteMapping("/{id}")
    public List<ProductDto> delete(@PathVariable UUID id) {
        productService.delete(id);
        return productService.findAll();
    }

    @PostMapping("/products/{productId}/ratings")
    public ResponseEntity<?> addRating(
            @PathVariable UUID productId,
            @RequestParam UUID userId,
            @RequestParam Integer stars
    ) {
        return productService.addRating(productId, userId, stars);
    }

    @GetMapping("/products/{productId}/ratings")
    public ResponseEntity<List<RatingDto>> getRatings(@PathVariable UUID productId) {
        return productService.getRatings(productId);
    }

    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<ProductDto> updateQuantity(
            @PathVariable UUID productId,
            @RequestParam Integer quantity
    ) {
        ProductDto updatedProduct = productService.updateQuantity(productId, quantity);
        return ResponseEntity.ok(updatedProduct);
    }






}
