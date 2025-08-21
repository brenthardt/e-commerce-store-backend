package org.example.ecommercestore_backend.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.ecommercestore_backend.dto.*;
import org.example.ecommercestore_backend.entity.User;
import org.example.ecommercestore_backend.jwt.JwtUtil;
import org.example.ecommercestore_backend.repository.RoleRepository;
import org.example.ecommercestore_backend.repository.UserRepository;
import org.example.ecommercestore_backend.role.Role;
import org.example.ecommercestore_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private UserDto2 mapToUserDto2(User user) {
        UserDto2 dto = new UserDto2();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());

        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .toList());
        }

        if (user.getWishList() != null) {
            List<WishListDto> wishListDtos = user.getWishList().stream().map(wishList -> {
                WishListDto w = new WishListDto();
                w.setTitle(wishList.getTitle());
                w.setPrice(wishList.getPrice());
                w.setProductId(wishList.getProductId());
                return w;
            }).toList();
            dto.setWishList(wishListDtos);
        }

        if (user.getRatings() != null) {
            dto.setRatings(user.getRatings().stream().map(rating -> new RatingDto()).toList());
        }

        return dto;
    }


    @Override
    public ResponseEntity<?> findAll() {
        List<UserDto2> dtos = userRepository.findAll().stream()
                .map(this::mapToUserDto2)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Override
    public User save(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            user.setRoles(List.of(userRole));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> update(UUID id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setPhone(user.getPhone());
                    existingUser.setAddress(user.getAddress());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRoles(user.getRoles());

                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> signUp(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists with this email");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.setRoles(List.of(userRole));

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail(), "ROLE_USER");
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getEmail(), "ROLE_USER");

        savedUser.setRefreshToken(refreshToken);
        userRepository.save(savedUser);

        LoginResponse response = new LoginResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getPhone(),
                savedUser.getEmail(),
                "ROLE_USER",
                token,
                refreshToken
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDTO) {
        return userRepository.findByEmail(loginDTO.getEmail())
                .map(user -> {
                    if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                        String mainRole = user.getRoles().stream()
                                .findFirst()
                                .map(Role::getName)
                                .orElse("UNKNOWN");

                        String token = jwtUtil.generateToken(user.getEmail(), mainRole);
                        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), mainRole);



                        user.setRefreshToken(refreshToken);
                        userRepository.save(user);



                        LoginResponse response = new LoginResponse(
                                user.getId(),
                                user.getName(),
                                user.getPhone(),
                                user.getEmail(),
                                mainRole,
                                token,
                                refreshToken
                        );

                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.badRequest().body("User not found"));
    }

    @Override
    public ResponseEntity<?> logout(String email) {
        return ResponseEntity.ok("User logged out");
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {
        try {
            Claims claims = jwtUtil.getClaims(refreshToken);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Invalid refresh token (not found in DB)"));

            if (!user.getEmail().equals(email)) {
                return ResponseEntity.status(401).body("Token subject mismatch");
            }

            String newAccessToken = jwtUtil.generateToken(email, role);

            String newRefreshToken = jwtUtil.generateRefreshToken(email, role);
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);

            System.out.println("New tokens generated and saved");

            return ResponseEntity.ok(
                    new LoginResponse(
                            user.getId(),
                            user.getName(),
                            user.getPhone(),
                            user.getEmail(),
                            role,
                            newAccessToken,
                            newRefreshToken
                    )
            );

        } catch (Exception e) {
            System.out.println(" Exception during refresh: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(403).body("Refresh token expired or invalid");
        }
    }


}
