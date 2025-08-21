package org.example.ecommercestore_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.CartDto;
import org.example.ecommercestore_backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping
    public ResponseEntity<List<CartDto>> findAll(@RequestParam UUID userId) {
        return ResponseEntity.ok(cartService.findAll(userId));
    }


    @PostMapping
    public ResponseEntity<CartDto> save(@RequestParam String title, @RequestParam Integer price,@RequestParam Integer quantity,
                                        @RequestParam UUID userId,  @RequestParam UUID productId)
    {
        CartDto saved = cartService.save(title, price,quantity, userId,productId);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{cartId}/quantity")
    public ResponseEntity<CartDto> updateQuantity(
            @PathVariable UUID cartId,
            @RequestParam Integer quantity,
            @RequestParam UUID userId
    ) {
        CartDto updatedCart = cartService.updateQuantity(cartId, quantity, userId);
        return ResponseEntity.ok(updatedCart);
    }


}
