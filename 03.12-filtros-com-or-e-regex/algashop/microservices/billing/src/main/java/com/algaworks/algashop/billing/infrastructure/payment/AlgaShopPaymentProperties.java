package com.algaworks.algashop.billing.infrastructure.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("algashop.integrations.payment")
@Data
@Validated
public class AlgaShopPaymentProperties {
    @NotNull
    private AlgaShopPaymentProvider provider;

    @NotNull
    @Valid
    private FastpayProperties fastpay;

    public enum AlgaShopPaymentProvider {
        FAKE,
        FASTPAY
    }

    @Validated
    @Data
    public static class FastpayProperties {
        @NotBlank
        private String hostname;

        @NotBlank
        private String privateToken;

        @NotBlank
        private String webhookUrl;
    }
}
