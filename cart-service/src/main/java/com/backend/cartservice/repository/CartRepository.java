package com.backend.cartservice.repository;

import com.backend.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId); // Trả về Optional để xử lý trường hợp không tìm thấy giỏ hàng
}
