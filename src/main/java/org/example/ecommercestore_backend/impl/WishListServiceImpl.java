package org.example.ecommercestore_backend.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.ImageDto;
import org.example.ecommercestore_backend.dto.WishListDto;
import org.example.ecommercestore_backend.entity.Product;
import org.example.ecommercestore_backend.entity.WishList;
import org.example.ecommercestore_backend.entity.User;
import org.example.ecommercestore_backend.repository.ProductRepository;
import org.example.ecommercestore_backend.repository.UserRepository;
import org.example.ecommercestore_backend.repository.WishListRepository;
import org.example.ecommercestore_backend.service.WishListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public List<WishListDto> findAll(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WishList> wishLists = wishListRepository.findAllByUser(user);

        return wishLists.stream().map(wishList -> {
            Product product = productRepository.findById(wishList.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            List<ImageDto> imageDtos = product.getImages().stream()
                    .map(img -> new ImageDto(img.getId(), img.getTitle(), img.getPath()))
                    .collect(Collectors.toList());

            return new WishListDto(
                    wishList.getId(),
                    wishList.getTitle(),
                    wishList.getPrice(),
                    wishList.getProductId(),
                    imageDtos
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WishListDto save(String title, Integer price, UUID userId, UUID productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (wishListRepository.findByProductIdAndUser(productId, user).isPresent()) {
            throw new RuntimeException("Product already in wishlist");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishList = new WishList();
        wishList.setTitle(title);
        wishList.setPrice(price);
        wishList.setUser(user);
        wishList.setProductId(productId);

        WishList saved = wishListRepository.save(wishList);

        List<ImageDto> imageDtos = product.getImages().stream()
                .map(img -> new ImageDto(img.getId(), img.getTitle(), img.getPath()))
                .collect(Collectors.toList());

        return new WishListDto(
                saved.getId(),
                saved.getTitle(),
                saved.getPrice(),
                saved.getProductId(),
                imageDtos
        );
    }


    @Override
    @Transactional
    public void delete(UUID id) {
        // Add logging to verify the deletion is being called
        System.out.println("Deleting wishlist item with ID: " + id);

        WishList wishList = wishListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WishList not found"));

        // Add logging to verify the item exists
        System.out.println("Found wishlist item to delete: " + wishList.getId());

        wishListRepository.delete(wishList);

        // Verify deletion by trying to find it again
        boolean existsAfterDelete = wishListRepository.existsById(id);
        System.out.println("Item still exists after delete? " + existsAfterDelete);
    }


}
