package com.backend.productservice.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    List<String> getAllImages();
    boolean deleteImageByName(String publicId);
}
