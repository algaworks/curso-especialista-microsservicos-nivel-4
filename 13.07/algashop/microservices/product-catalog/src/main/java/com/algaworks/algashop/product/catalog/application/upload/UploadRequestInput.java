package com.algaworks.algashop.product.catalog.application.upload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadRequestInput {

    @NotBlank
    private String originalFileName;

    @NotNull
    private Long contentLength;

}
