package org.example.ecommercestore_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "image")
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String path;

    @ManyToOne
    @JsonBackReference
    private Product product;

    public Image(String originalFilename, String path, Product product) {
        this.title = originalFilename;
        this.path = path;
        this.product = product;
    }
}