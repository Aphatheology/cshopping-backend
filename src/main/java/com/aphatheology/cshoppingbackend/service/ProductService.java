package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.dto.ProductDto;
import com.aphatheology.cshoppingbackend.entity.Images;
import com.aphatheology.cshoppingbackend.entity.Products;
import com.aphatheology.cshoppingbackend.entity.Tags;
import com.aphatheology.cshoppingbackend.exception.ResourceNotFoundException;
import com.aphatheology.cshoppingbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    public Products map2Entity(ProductDto productDto) {
        Products products = new Products();
        products.setTitle(productDto.getTitle());
        products.setDescription(productDto.getDescription());
        products.setTags(productDto.getTags());

        return products;
    }

    public Products createProduct(ProductDto productDto, MultipartFile[] files) {
        Set<Images> productImages = null;
        if(files.length > 0) {
            productImages = this.imageService.uploadImages(files);
        }

        Products product = this.map2Entity(productDto);
        product.setImages(productImages);

        return this.productRepository.save(product);
    }

    public List<Products> getProducts() {
        return this.productRepository.findAll();
    }

    public Products getProductById(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID " + productId));
    }

    public List<Products> getProductsByTags(List<Tags> tags) {
        return this.productRepository.findAllByTagsIn(tags);
    }

    public void deleteProduct(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        productRepository.delete(product);
        //delete images on cloudinary
    }

    public Products updateProduct(Long productId, ProductDto updatedProduct, MultipartFile[] files) {
        Products existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        Set<Images> productImages;

        if(files.length > 0) {
            productImages = this.imageService.uploadImages(files);
            Set<Images> existingImages = existingProduct.getImages();
            existingImages.addAll(productImages);
            existingProduct.setImages(existingImages);
        }

        this.modelMapper.map(updatedProduct, existingProduct);

        return productRepository.save(existingProduct);
    }

}
