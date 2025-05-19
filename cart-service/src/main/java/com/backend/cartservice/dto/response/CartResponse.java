package com.backend.cartservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    private Long id;
    private Long customerId;
    private double totalPrice;
    private Integer count;
}
