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

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "products_tags",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id")
            })
    @JsonIgnore
    private Set<Tags> tags;

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

    @Transient
    private List<String> tagNames;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void setImagesUrl() {
        this.imagesUrl = new ArrayList<>();
        for (Images image : images) {
            this.imagesUrl.add(image.getUrl());
        }

        this.tagNames = new ArrayList<>();
        for (Tags tag : tags) {
            this.tagNames.add(tag.getName());
        }
    }
}
