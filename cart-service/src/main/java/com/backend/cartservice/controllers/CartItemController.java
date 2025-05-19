package com.backend.cartservice.controllers;

import com.backend.cartservice.dto.request.CreateCartItem;
import com.backend.cartservice.dto.request.UpdateCartItem;
import com.backend.cartservice.dto.response.CartItemReponse;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.services.CartItemService;
import com.backend.commonservice.dto.reponse.ProductReponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private static final Logger log = LoggerFactory.getLogger(CartItemController.class);
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // API để thêm chi tiết giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<ApiResponseDTO<CartItemReponse>> addCartItem(@RequestBody @Valid CreateCartItem cartItem) {
        CartItemReponse newCartItem = cartItemService.addCartItem(cartItem);
        ApiResponseDTO<CartItemReponse> response = new ApiResponseDTO<>();
        response.setCode(HttpStatus.CREATED.value());
        response.setMessage("Thêm chi tiết giỏ hàng thành công");
        response.setData(newCartItem);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // API để lấy tất cả chi tiết giỏ hàng của một giỏ hàng
    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartItemReponse>> getCartItems(@PathVariable Long cartId) throws JsonProcessingException {
        List<CartItem> cartItems = cartItemService.getCartItems(cartId);

        List<CartItemReponse> cartItemReponseList = new ArrayList<>();

        for (CartItem item: cartItems){
            ProductReponse productReponse = findProductId(item.getProductId());

            CartItemReponse reponse = CartItemReponse.builder()
                    .id(item.getId())
                    .cartId(cartId)
                    .productId(item.getProductId())
                    .price(productReponse.getGiaBan())
                    .quantity(item.getQuantity())
                    .name(productReponse.getTensp())
                    .image(productReponse.getHinhAnh())
                    .build();

            cartItemReponseList.add(reponse);
        }

        return ResponseEntity.ok(cartItemReponseList); // Trả về danh sách các chi tiết giỏ hàng
    }



    private ProductReponse findProductId(Long id){
        try {
//            String URL_FIND_PRODUCT_BY_ID = "http://localhost:9898/products/1";

            String URL_FIND_PRODUCT_BY_ID = UriComponentsBuilder.fromHttpUrl("http://localhost:9898/products/{id}")
                    .buildAndExpand(id)
                    .toUriString();

            log.error(">>>>>>>>>>>>>> url = " + URL_FIND_PRODUCT_BY_ID);
            RestTemplate restTemplate = new RestTemplate();
            String productResponse = restTemplate.getForObject(URL_FIND_PRODUCT_BY_ID, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            // Parse root object
            Map<String, Object> root = objectMapper.readValue(productResponse, Map.class);

            // Lấy ra phần "data"
            Map<String, Object> data = (Map<String, Object>) root.get("data");

            // Chuyển vào ProductResponse
            ProductReponse product = objectMapper.convertValue(data, ProductReponse.class);

            log.error(">>>>>>>>>>>>>>>>product response = " + productResponse);

            return product;
        }catch (Exception e){
            return new ProductReponse();
        }
    }

    // API để cập nhật chi tiết giỏ hàng
    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<CartItemReponse>> updateCartItem(@RequestBody @Valid UpdateCartItem cartItem) {
        CartItemReponse updatedCartItem = cartItemService.updateCartItem(cartItem);
//        return updatedCartItem != null
//                ? ResponseEntity.ok(updatedCartItem) // Trả về chi tiết giỏ hàng đã cập nhật
//                : ResponseEntity.notFound().build(); // Trả về lỗi nếu không tìm thấy
        ApiResponseDTO<CartItemReponse> response = new ApiResponseDTO<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Cập nhật chi tiết giỏ hàng thành công");
        response.setData(updatedCartItem);
        return ResponseEntity.ok(response); // Trả về chi tiết giỏ hàng đã cập nhật
    }

    // API để xóa chi tiết giỏ hàng
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteCartItem(@PathVariable Long cartItemId) {
        boolean check = cartItemService.deleteCartItem(cartItemId);
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Xóa sản phẩm trong giỏ hàng thành công");
        response.setData(check + "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/plus/{cartItemId}")
    public ResponseEntity<String> plus(@PathVariable Long cartItemId){
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        cartItemService.plus(cartItem);
        return new ResponseEntity<>("Plus ok", HttpStatus.OK);
    }

    @PostMapping("/minus/{cartItemId}")
    public ResponseEntity<String> minus(@PathVariable Long cartItemId){
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        cartItemService.minus(cartItem);
        return new ResponseEntity<>("Minus ok", HttpStatus.OK);
    }


}
