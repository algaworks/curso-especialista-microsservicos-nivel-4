package com.algaworks.algashop.product.catalog.infrastructure.storage.fake;

import com.algaworks.algashop.product.catalog.application.storage.FileReference;
import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Component
public class StorageProviderFakeImpl implements StorageProvider {

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
        return false;
    }
}
