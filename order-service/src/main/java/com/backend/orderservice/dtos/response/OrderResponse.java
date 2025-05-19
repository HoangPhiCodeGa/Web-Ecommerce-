package com.backend.orderservice.dtos.response;

import com.backend.orderservice.dtos.OrderDetailDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponse {
    Long id;
    LocalDate ngayDatHang;
    Double tongTien;
    String status;
    Long customerId;

    private List<OrderDetailDTO> orderDetailDTOS;
}
