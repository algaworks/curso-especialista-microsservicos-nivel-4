package com.algaworks.algashop.ordering.core.ports.in.order;

import com.algaworks.algashop.ordering.core.ports.in.commons.AddressData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInput {
    @Valid
    @NotNull
    private RecipientData recipient;

    @Valid
    @NotNull
    private AddressData address;
}
