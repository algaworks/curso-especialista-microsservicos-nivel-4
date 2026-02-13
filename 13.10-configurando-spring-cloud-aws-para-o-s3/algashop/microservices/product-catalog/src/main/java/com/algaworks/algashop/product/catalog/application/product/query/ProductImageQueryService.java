package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.DomainEntityNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    public List<ImageOutput> getAllImages(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return product.getImages().stream()
                .map(i -> mapper.convert(i, ImageOutput.class)).toList();
    }

    public ImageOutput getImage(UUID productId, UUID imageId) {
        return this.getAllImages(productId).stream().filter(i -> i.getId().equals(imageId))
                .findFirst().orElseThrow(()-> new DomainEntityNotFoundException());
    }

}
