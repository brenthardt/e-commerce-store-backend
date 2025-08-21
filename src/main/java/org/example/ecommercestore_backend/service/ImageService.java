package org.example.ecommercestore_backend.service;

import org.example.ecommercestore_backend.entity.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    List<Image> findAll();
}
