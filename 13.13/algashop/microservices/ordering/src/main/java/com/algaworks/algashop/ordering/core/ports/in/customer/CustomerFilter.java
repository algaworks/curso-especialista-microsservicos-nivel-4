package com.algaworks.algashop.ordering.core.ports.in.customer;

import com.algaworks.algashop.ordering.core.application.utility.SortablePageFilter;
import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFilter
        extends SortablePageFilter<CustomerFilter.SortType> {
    private String email;
    private String firstName;

    public CustomerFilter(int size, int page) {
        super(size, page);
    }

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? SortType.REGISTERED_AT : getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        REGISTERED_AT("registeredAt"),
        FIRST_NAME("firstName");

        private final String propertyName;
    }
}
