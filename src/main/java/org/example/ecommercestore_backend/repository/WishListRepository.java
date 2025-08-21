package org.example.ecommercestore_backend.repository;

import org.example.ecommercestore_backend.entity.User;
import org.example.ecommercestore_backend.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishListRepository extends JpaRepository<WishList, UUID> {
    Optional<WishList> findByProductIdAndUser(UUID productId, User user);
    List<WishList> findAllByUser(User user);
    void deleteByProductId(UUID productId);

}
