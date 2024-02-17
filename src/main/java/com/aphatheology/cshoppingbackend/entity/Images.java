package com.aphatheology.cshoppingbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    private String name;
    private String type;
    private String url;
    private String cloudId;

}
