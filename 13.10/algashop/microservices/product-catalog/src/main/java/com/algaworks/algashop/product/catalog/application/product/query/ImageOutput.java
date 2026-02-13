package com.algaworks.algashop.product.catalog.application.product.query;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ImageOutput implements Serializable {
    private UUID id;
    private String url;
}
