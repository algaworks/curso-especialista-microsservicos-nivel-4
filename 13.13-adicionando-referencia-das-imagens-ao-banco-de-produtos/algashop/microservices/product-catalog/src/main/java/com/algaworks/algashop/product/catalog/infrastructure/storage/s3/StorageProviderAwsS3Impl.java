package com.algaworks.algashop.product.catalog.infrastructure.storage.s3;

import com.algaworks.algashop.product.catalog.application.storage.FileReference;
import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Exception;
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
        String bucketName = properties.getBucketName();
        String key = fileReference.getFileName();

        if (fileExists(key)) {
            throw new StorageProviderException(String.format("Remote file %s already exists", key));
        }

        ObjectMetadata.Builder metadataBuilder = ObjectMetadata.builder();

        if (fileReference.isAllowPublicRead()) {
            metadataBuilder.acl("public-read");
        }

        try {
            return s3Template.createSignedPutURL(
                    bucketName,
                    key,
                    fileReference.getExpiresIn(),
                    metadataBuilder.build(),
                    fileReference.getContentType().toString()
            );
        } catch (S3Exception e) {
            throw new StorageProviderException(String.format("Unknown error when tried to create" +
                    " presigned URL for file %s", key), e);
        }

    }

    @Override
    public void deleteFile(String remoteFileName) {
        if (!fileExists(remoteFileName)) {
            throw new StorageProviderException(
                    String.format("Remote file %s was not found", remoteFileName));
        }

        try {
            s3Template.deleteObject(properties.getBucketName(), remoteFileName);
        } catch (S3Exception e) {
            throw new StorageProviderException(
                    String.format("Unknown error when tried to remove the file %s", remoteFileName));
        }
    }

    @Override
    public boolean fileExists(String remoteFileName) {
        return s3Template.objectExists(properties.getBucketName(), remoteFileName);
    }
}
