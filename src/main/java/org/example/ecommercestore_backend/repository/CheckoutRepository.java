package org.example.ecommercestore_backend.repository;

import org.example.ecommercestore_backend.entity.Cart;
import org.example.ecommercestore_backend.entity.CheckOut;
import org.example.ecommercestore_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckOut, UUID> {
    Optional<CheckOut> findByUserAndProductId(User user, UUID productId);
    List<CheckOut> findAllByUser(User user);
}
