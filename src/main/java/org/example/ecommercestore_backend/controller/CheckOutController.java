package org.example.ecommercestore_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.CheckOutDto;
import org.example.ecommercestore_backend.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckOutController {

    private final CheckoutService checkoutService;

    @GetMapping
    public ResponseEntity<List<CheckOutDto>> findAll(@RequestParam UUID userId) {
        return ResponseEntity.ok(checkoutService.findAll(userId));
    }

    @PostMapping
    public ResponseEntity<CheckOutDto> save(@RequestBody CheckOutDto dto) {
        CheckOutDto saved = checkoutService.save(
                dto.getName(),
                dto.getCompany(),
                dto.getStreetAddress(),
                dto.getApartment(),
                dto.getCity(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getUserId(),
                dto.getProductId(),
                dto.getQuantity()
        );
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        checkoutService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
