package org.example.ecommercestore_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishListDto {
    private UUID id;
    private String title;
    private Integer price;
    private UUID productId;
    private List<ImageDto> images;
}
