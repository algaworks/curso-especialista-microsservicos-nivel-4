package com.algaworks.algashop.ordering.core.application.shipping;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShippingCostPreviewInput {
    @NotBlank
    @Size(min = 5, max = 5)
    private String zipCode;
}
