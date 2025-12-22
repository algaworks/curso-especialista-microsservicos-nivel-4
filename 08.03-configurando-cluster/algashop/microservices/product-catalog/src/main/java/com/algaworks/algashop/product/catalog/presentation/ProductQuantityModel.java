package com.algaworks.algashop.product.catalog.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductQuantityModel {

    @NotNull
    @Min(1)
    private Integer quantity;
}
