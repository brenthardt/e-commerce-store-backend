package org.example.ecommercestore_backend.service;

import org.example.ecommercestore_backend.dto.CheckOutDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CheckoutService {
    List<CheckOutDto> findAll(UUID userId);
    CheckOutDto save(String name, String company,String streetAddress, String apartment, String city, String phone, String email,UUID userId, UUID productId, Integer quantity);
    void delete(UUID id);
}
