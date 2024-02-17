package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.dto.ProductDto;
import com.aphatheology.cshoppingbackend.entity.Images;
import com.aphatheology.cshoppingbackend.entity.Products;
import com.aphatheology.cshoppingbackend.entity.Tags;
import com.aphatheology.cshoppingbackend.exception.ResourceNotFoundException;
import com.aphatheology.cshoppingbackend.repository.ImageRepository;
import com.aphatheology.cshoppingbackend.repository.ProductRepository;
import com.aphatheology.cshoppingbackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    public Products map2Entity(ProductDto productDto) {
        Products products = new Products();
        products.setTitle(productDto.getTitle());
        products.setDescription(productDto.getDescription());

        return products;
    }

    public Products createProduct(ProductDto productDto, MultipartFile[] files) {
        Set<Images> productImages = null;
        if(files.length > 0) {
            productImages = this.imageService.uploadImages(files);
        }

        Set<Tags> uniqueTags = new HashSet<>();

        for (String tagName : productDto.getTags()) {
            Tags tag = this.tagRepository.findByName(tagName);
            if (tag == null) {
                tag = new Tags();
                tag.setName(tagName);
                tag = tagRepository.save(tag);
            }
            uniqueTags.add(tag);
        }

        Products product = this.map2Entity(productDto);
        product.setImages(productImages);
        product.setTags(uniqueTags);

        return this.productRepository.save(product);
    }

    public List<Products> getProducts(List<String> tagNames) {
        List<Products> products;

        if (tagNames == null || tagNames.isEmpty()) {
            products = this.productRepository.findAll();
        } else {
            Set<Tags> tags = new HashSet<>();
            for (String tagName : tagNames) {
                Tags tag = tagRepository.findByName(tagName);
                if (tag != null) {
                    tags.add(tag);
                }
            }

            products = this.productRepository.findAllByTagsIn(tags);
        }

        return products;
    }

    public Products getProductById(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID " + productId));
    }

    public void deleteProduct(Long productId) throws IOException {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        for(Images image : product.getImages()) {
            this.cloudinaryService.deleteImage("cshopping/" + image.getCloudId());
        }

        productRepository.delete(product);
    }

    public Products updateProduct(Long productId, ProductDto productDto, MultipartFile[] files) {
        Products existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        Set<Images> productImages;

        if(files != null && files.length > 0) {
            productImages = this.imageService.uploadImages(files);
            Set<Images> existingImages = existingProduct.getImages();
            existingImages.addAll(productImages);
            existingProduct.setImages(existingImages);
        }

        if(productDto != null) {
            this.modelMapper.map(productDto, existingProduct);
            if(productDto.getTags() != null) {
                Set<Tags> tags = new HashSet<>();
                for (String tagName : productDto.getTags()) {
                    Tags tag = this.tagRepository.findByName(tagName);
                    if (tag == null) {
                        tag = new Tags();
                        tag.setName(tagName);
                        tagRepository.save(tag);
                    }
                    tags.add(tag);
                }
                existingProduct.setTags(tags);
            }
        }

        return productRepository.save(existingProduct);
    }

    public void deleteProductImage(String cloudId) throws IOException {
        Images productImage = this.imageRepository.findDistinctByCloudId(cloudId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Image not found with Cloud ID: " + cloudId));

        Products product = productRepository.findDistinctByImagesContaining(productImage);
        product.getImages().remove(productImage);

        this.cloudinaryService.deleteImage("cshopping/" + productImage.getCloudId());
        this.productRepository.save(product);
        this.imageRepository.delete(productImage);
    }
}
