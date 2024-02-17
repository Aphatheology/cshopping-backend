package com.aphatheology.cshoppingbackend.controller;

import com.aphatheology.cshoppingbackend.dto.ApiResponse;
import com.aphatheology.cshoppingbackend.dto.ProductDto;
import com.aphatheology.cshoppingbackend.entity.Products;
import com.aphatheology.cshoppingbackend.exception.BadRequestException;
import com.aphatheology.cshoppingbackend.exception.ResourceNotFoundException;
import com.aphatheology.cshoppingbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@RequestPart("product") ProductDto productDto, @RequestPart("imageFiles") MultipartFile[] files) {
        try {
            Products createdProduct = this.productService.createProduct(productDto, files);

            return new ResponseEntity<>(new ApiResponse(true, "Product Created Successfully", createdProduct), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProducts(@RequestParam(required = false) List<String> tags) {
        try {
            System.out.println(tags);
            List<Products> products = this.productService.getProducts(tags);

            return new ResponseEntity<>(new ApiResponse(true, "Product Fetched Successfully", products), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Products product = this.productService.getProductById(productId);

            return new ResponseEntity<>(new ApiResponse(true, "Fetch Product by Id Successful", product), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            this.productService.deleteProduct(productId);
            return ResponseEntity.ok(new ApiResponse(true, "Product deleted successfully"));
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/image/{cloudId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String cloudId) {
        try {
            this.productService.deleteProductImage(cloudId);
            return ResponseEntity.ok(new ApiResponse(true, "Product Image deleted successfully"));
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestPart(required = false, name = "product") ProductDto productDto, @RequestPart(required = false, name = "imageList") MultipartFile[] files) {
        try {
            Products product = productService.updateProduct(productId, productDto, files);
            return ResponseEntity.ok(new ApiResponse(true, "Product updated successfully", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

}
