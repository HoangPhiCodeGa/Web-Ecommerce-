package com.backend.productservice.controllers;

import com.backend.productservice.services.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileController {
    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/upload_image")
    public String getProduct(MultipartFile image){
        return cloudinaryService.uploadImage(image);
    }

    @GetMapping("/get_images")
    public List<String> getAll(){
        return cloudinaryService.getAllImages();
    }

    @GetMapping("/delete_images")
    public boolean deleteImages(@RequestParam("name") String name){
        return cloudinaryService.deleteImageByName(name);
    }
}
