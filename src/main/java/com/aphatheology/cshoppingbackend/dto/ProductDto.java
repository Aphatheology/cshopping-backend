package com.aphatheology.cshoppingbackend.dto;

import com.aphatheology.cshoppingbackend.entity.Tags;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotBlank(message = "Product title cannot be blank")
    private String title;

    private String description;

    private List<String> imagesUrl;

    @NotNull
    private List<Tags> tags;
}
