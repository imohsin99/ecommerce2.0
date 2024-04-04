package com.example.thymeleafapp.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageUploader {
    static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "alimajeed-dev",
            "api_key", "725321299395138",
            "api_secret", "G0BsOZi5hB2vhJxg9VexSeKioQc"));
     static String upload_profile_image(MultipartFile file) throws IOException {
         file.transferTo(new File("/home/dev/Ecommerce2.0/Ecommerce2.0/ThymeleafApp/src/main/resources/files/"+ file.getOriginalFilename()));
         File f = new File("/home/dev/Ecommerce2.0/Ecommerce2.0/ThymeleafApp/src/main/resources/files/" + file.getOriginalFilename());
         Map uploadResult = cloudinary.uploader().upload(f,ObjectUtils.emptyMap());
         return (String) uploadResult.get("secure_url");
    }

    static List<String> uploadProductImages(MultipartFile[] files) throws IOException {
        List<String> files_path= new ArrayList<>();
        if (files.length == 0){
             return files_path;
         }
        for (MultipartFile file: files) {
            files_path.add(upload_profile_image(file));
        }
        return files_path;
    }

}
