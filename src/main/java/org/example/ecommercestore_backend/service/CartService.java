package org.example.ecommercestore_backend.service;

import org.example.ecommercestore_backend.dto.CartDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CartService {
    List<CartDto> findAll(UUID userId);
    CartDto save(String title, Integer price, Integer quantity, UUID userId,UUID productId);
    void delete(UUID id);
    CartDto updateQuantity(UUID cartId, Integer newQuantity, UUID userId);
}
