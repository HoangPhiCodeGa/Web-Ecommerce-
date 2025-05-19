package com.backend.cartservice.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/10/2025 10:24 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemReponse {
    Long id;
    Long cartId;
    Long productId;
    double price;
    int quantity;
    private String name;
    private String image;
    Long version;
}
