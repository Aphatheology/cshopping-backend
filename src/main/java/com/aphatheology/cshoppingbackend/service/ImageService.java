package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.entity.Images;
import com.aphatheology.cshoppingbackend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public Set<Images> uploadImages(MultipartFile[] files) {
        Set<Images> uploadedImages = new HashSet<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                continue;
            }

            Images image = new Images();
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            String url = this.cloudinaryService.uploadFile(file, "cshopping");
            image.setUrl(url);

            String regex = ".*/cshopping/(.*)";
            image.setCloudId(url.replaceAll(regex, "$1"));

            if (image.getUrl() == null) {
                continue;
            }

            this.imageRepository.save(image);
            uploadedImages.add(image);
        }

        return uploadedImages;
    }
}
