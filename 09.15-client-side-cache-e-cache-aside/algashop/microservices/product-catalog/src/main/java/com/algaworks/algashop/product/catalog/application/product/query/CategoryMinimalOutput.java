package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.infrastructure.utility.Slugfier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryMinimalOutput implements Serializable {
    private UUID id;
    private String name;
    private Boolean enabled;

    public String getSlug() {
        return Slugfier.slugify(this.getName());
    }
}
