package com.aphatheology.cshoppingbackend.controller;

import com.aphatheology.cshoppingbackend.dto.ApiResponse;
import com.aphatheology.cshoppingbackend.dto.ProductDto;
import com.aphatheology.cshoppingbackend.entity.Products;
import com.aphatheology.cshoppingbackend.exception.BadRequestException;
import com.aphatheology.cshoppingbackend.exception.ExistingEmailException;
import com.aphatheology.cshoppingbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@RequestPart("product") ProductDto productDto, @RequestPart("imageFiles")MultipartFile[] files) {
        try {
            Products createdProduct = this.productService.createProduct(productDto, files);

            return new ResponseEntity<>(new ApiResponse(true, "Product Created Successfully", createdProduct), HttpStatus.CREATED);
        } catch(BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProducts() {
        try {
            List<Products> products = this.productService.getProducts();

            return new ResponseEntity<>(new ApiResponse(true, "Product Fetched Successfully", products), HttpStatus.OK);
        } catch(BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
