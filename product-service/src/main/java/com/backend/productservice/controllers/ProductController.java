package com.backend.productservice.controllers;


import com.backend.productservice.dto.ProductDTO;
import com.backend.productservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @description
 * @author: pham kim khuong
 * @version: 1.0
 * @created: 2/21/2025 1:07 PM
 */

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Query", description = "Product API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Get List Product",
            description = "Get all Product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String keyword) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        if (keyword == null || keyword.isEmpty()) {
            response.put("data", productService.getAllProduct());
        } else {
//            response.put("data", productService.search(keyword));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Add Product",
            description = "Save a Product to database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@Valid @RequestBody ProductDTO productDTO
    ) {
        productService.saveProduct(productDTO);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete Product",
            description = "Delete a Product from database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.deleteProduct(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Update Product",
            description = "Update a Product from database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO
    ) {
        productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }

    @Operation(
            summary = "Get Product by ID",
            description = "Get a Product by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.getProductById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
