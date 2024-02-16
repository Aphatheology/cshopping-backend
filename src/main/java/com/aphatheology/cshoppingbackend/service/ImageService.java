package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.entity.Images;
import com.aphatheology.cshoppingbackend.exception.BadRequestException;
import com.aphatheology.cshoppingbackend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename().isEmpty()) throw new BadRequestException("Invalid file or filename");

        Images image = new Images();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setUrl(this.cloudinaryService.uploadFile(file, "cshopping"));

        if(image.getUrl() == null) throw new BadRequestException("Invalid file");

        this.imageRepository.save(image);
        return image.getUrl();
    }

    public Set<Images> uploadImages(MultipartFile[] files) {
        Set<Images> uploadedImages = new HashSet<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || file.getOriginalFilename().isEmpty()) {
                continue;
            }

            Images image = new Images();
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setUrl(this.cloudinaryService.uploadFile(file, "cshopping"));

            if (image.getUrl() == null) {
                continue;
            }

            this.imageRepository.save(image);
            uploadedImages.add(image);
        }

//        if(uploadedImages.isEmpty()) throw new BadRequestException("Invalid files");

        return uploadedImages;
    }
}
