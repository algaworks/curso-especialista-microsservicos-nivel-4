package com.algaworks.algashop.product.catalog.application;

import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageModel<T> {
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;

    @Builder.Default
    private List<T> content = new ArrayList<>();

    public static <T> PageModel<T> of(Page<T> page) {
        return PageModel.<T>builder()
                .content(page.getContent())
                .number(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
