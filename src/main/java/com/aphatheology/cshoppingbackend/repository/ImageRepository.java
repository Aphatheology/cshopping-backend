package com.aphatheology.cshoppingbackend.repository;

import com.aphatheology.cshoppingbackend.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Images, UUID> {
    Optional<Images> findDistinctByCloudId(String cloudId);
}
