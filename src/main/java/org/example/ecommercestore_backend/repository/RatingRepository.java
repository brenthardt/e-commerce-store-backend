package org.example.ecommercestore_backend.repository;

import org.example.ecommercestore_backend.entity.Product;
import org.example.ecommercestore_backend.entity.Rating;
import org.example.ecommercestore_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByUserAndProduct(User user, Product product);
    List<Rating> findAllByProductId(UUID productId);
}
