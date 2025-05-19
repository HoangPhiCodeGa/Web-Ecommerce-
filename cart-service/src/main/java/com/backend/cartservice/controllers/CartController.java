package com.backend.cartservice.controllers;

import com.backend.cartservice.dto.response.CartResponse;
import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.services.CartService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // API để thêm giỏ hàng mới (sử dụng customerId từ CartRequest)
    @PostMapping()
    public ResponseEntity<Cart> addCart(@RequestBody @Valid CartRequest cartRequest
    ) {
        log.info("Thêm giỏ hàng mới cho customerId: {}", cartRequest.getCustomerId());
        Cart cart = cartService.addCart(cartRequest.getCustomerId());
        return ResponseEntity.ok(cart);
    }

    // API để lấy giỏ hàng của khách hàng theo customerId
    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {

        Optional<Cart> cart = cartService.getCart(customerId);

        if (cart.isPresent()) {
            Cart c = cart.get();
            c.updateTotalPrice();
            CartResponse cartResponse = CartResponse.builder()
                    .id(c.getId())
                    .customerId(c.getCustomerId())
                    .totalPrice(c.getTotalPrice())
                    .count(c.getCartItems().size())
                    .build();
            return ResponseEntity.ok(cartResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count/{customerId}")
    public int getCountCart(@PathVariable Long customerId){
        try {
            Optional<Cart> cart = cartService.getCart(customerId);

            if (cart.isPresent()) {
                return cart.get().getCartItems().size();
            } else {
                return 0;
            }

        }catch (Exception e){
            return 0;
        }
    }

    // API để cập nhật giỏ hàng
    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long cartId, @RequestBody Cart cart) {
        // Cập nhật giỏ hàng theo cartId
        Cart updatedCart = cartService.updateCart(cartId, cart);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // API để xóa giỏ hàng
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        // Lấy thông tin giỏ hàng để kiểm tra tồn tại
        Optional<Cart> cart = cartService.getCart(cartId);
        if (cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
