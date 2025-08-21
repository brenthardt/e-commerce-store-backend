package org.example.ecommercestore_backend.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.entity.Image;
import org.example.ecommercestore_backend.repository.ImageRepository;
import org.example.ecommercestore_backend.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

private final ImageRepository imageRepository;

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}
