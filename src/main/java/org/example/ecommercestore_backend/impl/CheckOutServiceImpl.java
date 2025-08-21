package org.example.ecommercestore_backend.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.CheckOutDto;
import org.example.ecommercestore_backend.dto.ImageDto;
import org.example.ecommercestore_backend.entity.CheckOut;
import org.example.ecommercestore_backend.entity.Product;
import org.example.ecommercestore_backend.entity.User;
import org.example.ecommercestore_backend.repository.CheckoutRepository;
import org.example.ecommercestore_backend.repository.ProductRepository;
import org.example.ecommercestore_backend.repository.UserRepository;
import org.example.ecommercestore_backend.service.CheckoutService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Override
    public List<CheckOutDto> findAll(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CheckOut> checkouts = checkoutRepository.findAllByUser(user);

        return checkouts.stream()
                .map(checkout -> {
                    Product product = productRepository.findById(checkout.getProductId()).orElse(null);
                    if (product == null) return null;

                    List<ImageDto> imageDtos = product.getImages().stream()
                            .map(img -> new ImageDto(img.getId(), img.getTitle(), img.getPath()))
                            .collect(Collectors.toList());

                    return new CheckOutDto(
                            checkout.getId(),
                            checkout.getName(),
                            checkout.getCompany(),
                            checkout.getStreetAddress(),
                            checkout.getApartment(),
                            checkout.getCity(),
                            checkout.getPhone(),
                            checkout.getEmail(),
                            checkout.getUser().getId(),
                            checkout.getProductId(),
                            imageDtos,
                            checkout.getQuantity()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public CheckOutDto save(String name, String company, String streetAddress, String apartment, String city, String phone, String email, UUID userId, UUID productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (quantity == null || quantity < 1) {
            throw new RuntimeException("Invalid quantity");
        }
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Deduct quantity
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        CheckOut checkout = new CheckOut();
        checkout.setName(name);
        checkout.setCompany(company);
        checkout.setStreetAddress(streetAddress);
        checkout.setApartment(apartment);
        checkout.setCity(city);
        checkout.setPhone(phone);
        checkout.setEmail(email);
        checkout.setUser(user);
        checkout.setProductId(productId);
        checkout.setQuantity(quantity);
        checkoutRepository.save(checkout);

        List<ImageDto> imageDtos = product.getImages().stream()
                .map(img -> new ImageDto(img.getId(), img.getTitle(), img.getPath()))
                .collect(Collectors.toList());

        return new CheckOutDto(
                checkout.getId(),
                checkout.getName(),
                checkout.getCompany(),
                checkout.getStreetAddress(),
                checkout.getApartment(),
                checkout.getCity(),
                checkout.getPhone(),
                checkout.getEmail(),
                checkout.getUser().getId(),
                checkout.getProductId(),
                imageDtos,
                quantity
        );
    }

    @Override
    public void delete(UUID id) {
checkoutRepository.deleteById(id);
    }
}
