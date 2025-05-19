package com.backend.orderservice.service;

import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.response.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderResponse> getAll();

    OrderResponse getById(Long id);

    OrderResponse save(OrderDTO order);

    OrderResponse update(Long id, OrderDTO order);

    OrderResponse updateStatus(Long id, String status);

    boolean delete(Long id);

    Map<Integer, Double> staticIncome(int year);

    Map<Integer, Long> staticOrder(int year);
}
