package com.aphatheology.cshoppingbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private List<Tags> tags;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "products_images",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            })
    @JsonIgnore
    private Set<Images> images;

    @Transient
    private List<String> imagesUrl;

    @PostLoad
    @PostPersist
    public void setImagesUrl() {
        this.imagesUrl = new ArrayList<>();
        for (Images image : images) {
            this.imagesUrl.add(image.getUrl());
        }
    }
}
