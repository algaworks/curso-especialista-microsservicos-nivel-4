package com.algaworks.algashop.product.catalog.application.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UploadResponseOutput {
    private String uploadSignedUrl;
    private String remoteFileName;
    private Long contentLength;
    private String contentType;
    private OffsetDateTime expiresAt;
}
