package org.example.ecommercestore_backend.service;

import org.example.ecommercestore_backend.dto.WishListDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface WishListService {
    List<WishListDto> findAll(UUID userId);
    WishListDto save(String title, Integer price, UUID userId,UUID productId);
    void delete(UUID id);

}
