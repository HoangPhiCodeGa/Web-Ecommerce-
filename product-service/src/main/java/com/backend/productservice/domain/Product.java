package com.backend.productservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 21-February-2025 7:55 PM
 */

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "ten_sp", nullable = false,unique = true)
    private String tensp;
    @Column(name = "mo_ta",length = 1000)
    private String moTa;
    @Column(name = "hinh_anh")
    private String hinhAnh;
    @Column(name = "gia_ban", nullable = false)
    private Double giaBan;
    @Column(name = "gia_nhap", nullable = false)
    private Double giaNhap;
    @Column(name = "gia_goc", nullable = false)
    private Double giaGoc;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
}
