package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.dto.ProductDto;
import com.aphatheology.cshoppingbackend.entity.Images;
import com.aphatheology.cshoppingbackend.entity.Products;
import com.aphatheology.cshoppingbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;

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
}
