package com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@ConfigurationProperties("algashop.data-load")
@Data
@Validated
public class DataLoadProperties {

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean autoDelete;

    @Valid
    private List<DataLoadSource> sources;

    @Data
    public static class DataLoadSource {
        @NotBlank
        private String location;

        @NotBlank
        private String collection;
    }

}
