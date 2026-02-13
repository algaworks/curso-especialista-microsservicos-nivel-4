package com.algaworks.algashop.ordering.infrastructure.config.beans;

import com.algaworks.algashop.ordering.core.domain.model.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
    basePackages = "com.algaworks.algashop.ordering.core.domain.model",
    includeFilters = @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = DomainService.class
    )
)
public class DomainServiceScanConfig {
}
