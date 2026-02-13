package com.algaworks.algashop.ordering.core.application.shipping;

import com.algaworks.algashop.ordering.core.domain.model.commons.Address;
import com.algaworks.algashop.ordering.core.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingApplicationService {

    private final OriginAddressService originAddressService;
    private final ShippingCostService shippingCostService;

    public ShippingCostPreviewOutput previewCost(ShippingCostPreviewInput input) {
        Address originAddress = originAddressService.originAddress();

        var request = ShippingCostService.CalculationRequest.builder()
                .origin(originAddress.zipCode())
                .destination(new ZipCode(input.getZipCode()))
                .build();

        var result = shippingCostService.calculate(request);

        return new ShippingCostPreviewOutput(result.cost().value(), result.expectedDate());
    }

}
