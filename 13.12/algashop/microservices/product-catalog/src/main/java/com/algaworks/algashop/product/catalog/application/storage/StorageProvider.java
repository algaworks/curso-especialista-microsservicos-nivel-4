package com.algaworks.algashop.product.catalog.application.storage;

import java.net.URL;

public interface StorageProvider {
    boolean healthCheck();
    URL requestUploadUrl(FileReference fileReference);
    void deleteFile(String remoteFileName);
    boolean fileExists(String remoteFileName);
}
