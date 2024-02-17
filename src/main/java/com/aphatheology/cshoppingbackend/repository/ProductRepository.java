package com.aphatheology.cshoppingbackend.repository;

import com.aphatheology.cshoppingbackend.entity.Images;
import com.aphatheology.cshoppingbackend.entity.Products;
import com.aphatheology.cshoppingbackend.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    List<Products> findAllByTagsIn(Set<Tags> tags);
    Products findDistinctByImagesContaining(Images productImage);
}
