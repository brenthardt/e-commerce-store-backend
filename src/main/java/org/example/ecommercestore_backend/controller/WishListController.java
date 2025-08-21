package org.example.ecommercestore_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.WishListDto;
import org.example.ecommercestore_backend.service.WishListService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @GetMapping
    public ResponseEntity<List<WishListDto>> findAll(@RequestParam UUID userId) {
        return ResponseEntity.ok(wishListService.findAll(userId));
    }


    @PostMapping
    public ResponseEntity<WishListDto> save(@RequestParam String title, @RequestParam Integer price, @RequestParam UUID userId,  @RequestParam UUID productId)
    {
        WishListDto saved = wishListService.save(title, price, userId,productId);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        wishListService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
