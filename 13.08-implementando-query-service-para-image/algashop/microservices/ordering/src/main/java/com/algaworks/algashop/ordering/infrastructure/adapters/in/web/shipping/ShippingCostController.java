package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.shipping;

import com.algaworks.algashop.ordering.core.application.shipping.ShippingApplicationService;
import com.algaworks.algashop.ordering.core.application.shipping.ShippingCostPreviewInput;
import com.algaworks.algashop.ordering.core.application.shipping.ShippingCostPreviewOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShippingCostController {

    private final ShippingApplicationService shippingApplicationService;

    @PostMapping("/api/v1/shipping-cost-previews")
    public ShippingCostPreviewOutput previewShippingCost(@RequestBody @Valid ShippingCostPreviewInput input) {
        return shippingApplicationService.previewCost(input);
    }

}
