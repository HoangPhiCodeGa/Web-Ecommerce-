package com.backend.productservice.services.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.productservice.dto.reponse.ProductReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;
import com.backend.productservice.model.Category;
import com.backend.productservice.model.Product;
import com.backend.productservice.repository.CategorytRepository;
import com.backend.productservice.repository.ProductRepository;
import com.backend.productservice.services.CloudinaryService;
import com.backend.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Only create Contructor for final fields
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements ProductService {
    ProductRepository productRep;
    CategorytRepository categorytRep;
    ModelMapper modelMapper;
    CloudinaryService cloudinaryService;

    //    Convert Entity to DTO
    public ProductReponse toProductReponse(Product product) {
        return modelMapper.map(product, ProductReponse.class);
    }

    //    Convert DTO to Entity
    public Product toProduct(ProductCreationRequest product) {
        return modelMapper.map(product, Product.class);
    }

    @Override
    public List<ProductReponse> getAllProduct() {
        log.info("In method get all Product");
        return productRep.findAll().stream().map(this::toProductReponse).collect(Collectors.toList());
    }

    @Override
    public ProductReponse getProductById(Long id) {
        log.info("In method get Product by id");
        Product p = productRep.findById(id).orElseThrow(() -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        return toProductReponse(p);
    }

    @Transactional
    @Override
    public ProductReponse saveProduct(ProductCreationRequest product, MultipartFile hinhAnh) {
        log.info("ProductService: method save Product");
        Category c = categorytRep.findById(product.getCategory_id()).orElseThrow(() -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        Product p = toProduct(product);
        String hinhAnhURL = cloudinaryService.uploadImage(hinhAnh);
        p.setHinhAnh(hinhAnhURL);
        p.setCategory(c);
        productRep.save(p);
        return toProductReponse(p);
    }

    @Transactional
    @Override
    public ProductReponse updateProduct(Long id, ProductCreationRequest product) {
        log.info("In method update Product");
        Product p = productRep.findById(id).orElseThrow(() -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        return toProductReponse(productRep.save(p));
    }

    @Transactional
    @Override
    public boolean deleteProduct(Long id) {
        log.info("In method delete Product");
        productRep.deleteById(id);
        return true;
    }
}
