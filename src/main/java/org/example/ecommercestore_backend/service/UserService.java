package org.example.ecommercestore_backend.service;



import org.example.ecommercestore_backend.dto.LoginDto;
import org.example.ecommercestore_backend.dto.UserDto;
import org.example.ecommercestore_backend.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface UserService {
    ResponseEntity<?> findAll();
    User save(User user);
    ResponseEntity<?> delete(UUID id);
ResponseEntity<?> update(UUID id, User user);
    ResponseEntity<?> signUp(UserDto userDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponseEntity<?> logout(String phone);
    ResponseEntity<?> refreshToken(String refreshToken);
}
