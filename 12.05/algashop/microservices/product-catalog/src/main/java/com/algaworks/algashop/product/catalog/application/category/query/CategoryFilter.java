package com.algaworks.algashop.product.catalog.application.category.query;

import com.algaworks.algashop.product.catalog.application.utility.SortablePageFilter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CategoryFilter extends SortablePageFilter<CategoryFilter.SortType> {
    private String name;
    private Boolean enabled;

    @Override
    public CategoryFilter.SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? CategoryFilter.SortType.NAME: getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    public boolean isCacheable() {
        return isDefaultFilter();
    }

    private boolean isDefaultFilter() {
        return this.equals(defaultFilter());
    }

    public static CategoryFilter defaultFilter() {
        return CategoryFilter.builder()
                .name(null)
                .enabled(true)
                .page(0)
                .size(15)
                .sortDirection(Sort.Direction.ASC)
                .sortByProperty(SortType.NAME)
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        NAME("name");

        private final String propertyName;
    }
}
