package org.example.ecommercestore_backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "checkout")
@AllArgsConstructor
@NoArgsConstructor
public class CheckOut {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String company;
    private String streetAddress;
    private String apartment;
    private String city;
    private String phone;
    private String email;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private UUID productId;
    private Integer quantity;
}
