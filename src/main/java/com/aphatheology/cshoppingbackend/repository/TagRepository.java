package com.aphatheology.cshoppingbackend.repository;

import com.aphatheology.cshoppingbackend.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tags, Long> {


    Tags findByName(String tagName);
}
