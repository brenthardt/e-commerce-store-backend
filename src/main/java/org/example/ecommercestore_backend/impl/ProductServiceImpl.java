package org.example.ecommercestore_backend.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.ImageDto;
import org.example.ecommercestore_backend.dto.ProductDto;
import org.example.ecommercestore_backend.dto.RatingDto;
import org.example.ecommercestore_backend.entity.*;
import org.example.ecommercestore_backend.repository.ProductRepository;
import org.example.ecommercestore_backend.repository.RatingRepository;
import org.example.ecommercestore_backend.repository.UserRepository;
import org.example.ecommercestore_backend.repository.WishListRepository;
import org.example.ecommercestore_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Override
    @Transactional
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getColor(),
                product.getSize(),
                product.getQuantity(),
                product.getDiscount(),
                product.getCategory(),


                product.getAverageStars(),



                product.getRatings() != null
                        ? product.getRatings().stream()
                        .map(rating -> new RatingDto(
                                rating.getId(),
                                rating.getStars(),
                                rating.getUser().getId(),
                                rating.getProduct().getId()
                        ))
                        .collect(Collectors.toList())
                        : List.of(),


                product.getImages().stream()
                        .map(image -> new ImageDto(image.getId(), image.getTitle(), image.getPath()))
                        .collect(Collectors.toList())

        )).collect(Collectors.toList());
    }


    @Override
    public Product save(String title, Integer price, String description, String color,
                        String size, Integer quantity, Integer discount,
                        Category category, List<MultipartFile> images) {
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setColor(color);
        product.setSize(size);
        product.setQuantity(quantity);
        product.setDiscount(discount);
        product.setCategory(category);

        rabbit(images, product);
        System.out.println("HERE IS THE PRODUCT: "+product);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        for (Image image : product.getImages()) {
            try {
                Path imagePath = Paths.get(image.getPath());
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image file", e);
            }
        }
        wishListRepository.deleteByProductId(id);
        productRepository.delete(product);
    }

    @Override
    public Product update(UUID id, String title, Integer price, String description, String color, String size, Integer quantity, Integer discount,
                          Category category, List<MultipartFile> images) {
        Product product = productRepository.findById(id).orElseThrow();

        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setColor(color);
        product.setSize(size);
        product.setQuantity(quantity);
        product.setDiscount(discount);
        product.setCategory(category);

        if (images != null && !images.isEmpty()) {
            rabbit(images, product);
        }

        return productRepository.save(product);
    }

    public void rabbit(List<MultipartFile> images, Product product) {
        List<Image> newImages = new ArrayList<>();
        for (MultipartFile file : images) {
            String path = saveFile(file);
            Image image = new Image(file.getOriginalFilename(), path, product);
            newImages.add(image);
        }
        product.setImages(newImages);
    }

    public String saveFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get("files/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<List<RatingDto>> addRating(UUID productId, UUID userId, Integer stars) {
        if (stars < 1 || stars > 5) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Rating rating = ratingRepository.findByUserAndProduct(user, product)
                .orElse(new Rating());

        rating.setUser(user);
        rating.setProduct(product);
        rating.setStars(stars);

        ratingRepository.save(rating);

        List<Rating> ratings = ratingRepository.findAllByProductId(productId);
        double avgStars = ratings.stream().mapToInt(Rating::getStars).average().orElse(0.0);
        product.setAverageStars(avgStars);
        productRepository.save(product);


        List<RatingDto> ratingDtos = ratings.stream()
                .map(r -> new RatingDto(
                        r.getId(),
                        r.getStars(),
                        r.getUser().getId(),
                        r.getProduct().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ratingDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<List<RatingDto>> getRatings( UUID productId) {
        List<Rating> ratings = ratingRepository.findAllByProductId(productId);

        List<RatingDto> ratingDtos = ratings.stream()
                .map(rating -> new RatingDto(
                        rating.getId(),
                        rating.getStars(),
                        rating.getUser().getId(),
                        rating.getProduct().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ratingDtos);
    }

    @Override
    @Transactional
    public ProductDto updateQuantity(UUID productId, Integer newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (newQuantity < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        product.setQuantity(newQuantity);
        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getColor(),
                product.getSize(),
                product.getQuantity(),
                product.getDiscount(),
                product.getCategory(),
                product.getAverageStars(),
                product.getRatings() != null
                        ? product.getRatings().stream()
                        .map(rating -> new RatingDto(
                                rating.getId(),
                                rating.getStars(),
                                rating.getUser().getId(),
                                rating.getProduct().getId()
                        ))
                        .collect(Collectors.toList())
                        : List.of(),
                product.getImages().stream()
                        .map(image -> new ImageDto(image.getId(), image.getTitle(), image.getPath()))
                        .collect(Collectors.toList())
        );
    }

}
