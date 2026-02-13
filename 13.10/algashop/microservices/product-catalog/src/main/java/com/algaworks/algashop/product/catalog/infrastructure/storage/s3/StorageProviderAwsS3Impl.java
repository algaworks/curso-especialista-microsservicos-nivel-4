package com.algaworks.algashop.product.catalog.infrastructure.storage.s3;

import com.algaworks.algashop.product.catalog.application.storage.FileReference;
import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StorageProviderAwsS3Impl implements StorageProvider {

    private final StorageProviderAwsS3Properties properties;
    private final S3Template s3Template;

    @Override
    public boolean healthCheck() {
        try {
            return s3Template.bucketExists(properties.getBucketName());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @SneakyThrows
    public URL requestUploadUrl(FileReference fileReference) {
        return URI.create(String.format("http://localhost:4566/%s?token=%s",
                fileReference.getFileName(), UUID.randomUUID())).toURL();
    }

    @Override
    public void deleteFile(String remoteFileName) {

    }

    @Override
    public boolean fileExists(String remoteFileName) {
        return !remoteFileName.equals("fail.jpg");
    }
}
