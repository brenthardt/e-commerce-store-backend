package org.example.ecommercestore_backend.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.CartDto;
import org.example.ecommercestore_backend.dto.ImageDto;
import org.example.ecommercestore_backend.entity.Cart;
import org.example.ecommercestore_backend.entity.Product;
import org.example.ecommercestore_backend.entity.User;
import org.example.ecommercestore_backend.repository.CartRepository;
import org.example.ecommercestore_backend.repository.ProductRepository;
import org.example.ecommercestore_backend.repository.UserRepository;
import org.example.ecommercestore_backend.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public List<CartDto> findAll(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> carts = cartRepository.findAllByUser(user);

        return carts.stream()
                .map(cart -> {
                    Product product = productRepository.findById(cart.getProductId()).orElse(null);
                    if (product == null) return null;

                    List<ImageDto> imageDtos = product.getImages().stream()
                            .map(img -> new ImageDto(img.getId(), img.getTitle(), img.getPath()))
                            .collect(Collectors.toList());

                    return new CartDto(
                            cart.getId(),
                            cart.getTitle(),
                            cart.getPrice(),
                            cart.getQuantity(),
                            cart.getProductId(),
                            imageDtos
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDto save(String title, Integer price, Integer quantity, UUID userId, UUID productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart existingCart = cartRepository.findByUserAndProductId(user, productId)
                .orElse(null);

        int totalRequested = quantity;
        if (existingCart != null) {
            totalRequested += existingCart.getQuantity();
        }

        if (totalRequested > product.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            cartRepository.save(existingCart);
        } else {
            existingCart = new Cart();
            existingCart.setTitle(title);
            existingCart.setPrice(price);
            existingCart.setQuantity(quantity);
            existingCart.setUser(user);
            existingCart.setProductId(productId);
            cartRepository.save(existingCart);
        }

         product.setQuantity(product.getQuantity() - quantity);
         productRepository.save(product);

        return convertToDto(existingCart, product);
    }

    private CartDto convertToDto(Cart cart, Product product) {
        List<ImageDto> imageDtos = product.getImages().stream()
                .map(img -> new ImageDto(img.getId(), img.getTitle(), img.getPath()))
                .collect(Collectors.toList());

        return new CartDto(
                cart.getId(),
                cart.getTitle(),
                cart.getPrice(),
                cart.getQuantity(),
                cart.getProductId(),
                imageDtos
        );
    }

    @Override
    public void delete(UUID id) {
        Cart cart = cartRepository.findById(id)
                .orElse(null);
        if (cart != null) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElse(null);
            if (product != null) {
                product.setQuantity(product.getQuantity() + cart.getQuantity());
                productRepository.save(product);
            }
            cartRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public CartDto updateQuantity(UUID cartId, Integer newQuantity, UUID userId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this cart item");
        }

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (newQuantity < 1) {
            throw new RuntimeException("Quantity must be at least 1");
        }

        int oldQuantity = cart.getQuantity();
        int quantityDifference = newQuantity - oldQuantity;

        if (quantityDifference > 0 && quantityDifference > product.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        // Update product quantity
        product.setQuantity(product.getQuantity() - quantityDifference);
        productRepository.save(product);

        // Update cart quantity
        cart.setQuantity(newQuantity);
        cartRepository.save(cart);

        return convertToDto(cart, product);
    }


}
