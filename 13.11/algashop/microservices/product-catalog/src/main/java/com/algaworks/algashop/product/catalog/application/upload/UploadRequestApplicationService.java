package com.algaworks.algashop.product.catalog.application.upload;

import com.algaworks.algashop.product.catalog.application.storage.FileReference;
import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import com.algaworks.algashop.product.catalog.application.utility.ImageMediaTypeExtractor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadRequestApplicationService {

    private final StorageProvider storageProvider;

    public UploadResponseOutput requestPreSignedUrl(UploadRequestInput input) {
        MediaType mediaType = ImageMediaTypeExtractor.fromFileName(input.getOriginalFileName());

        if (!(mediaType.equals(MediaType.IMAGE_JPEG) || mediaType.equals(MediaType.IMAGE_PNG))) {
            throw new IllegalArgumentException("Invalid image type");
        }

        String extension = FilenameUtils.getExtension(input.getOriginalFileName());

        FileReference fileReference = FileReference.builder()
                .contentLength(input.getContentLength())
                .contentType(mediaType)
                .fileName(UUID.randomUUID() + "." + extension)
                .expiresIn(Duration.ofMinutes(5))
                .allowPublicRead(true)
                .build();

        URL presignedUrl = storageProvider.requestUploadUrl(fileReference);
        OffsetDateTime expiresAt = OffsetDateTime.now().plus(fileReference.getExpiresIn());

        return UploadResponseOutput.builder()
                .uploadSignedUrl(presignedUrl.toString())
                .remoteFileName(fileReference.getFileName())
                .contentLength(fileReference.getContentLength())
                .contentType(fileReference.getContentType().toString())
                .expiresAt(expiresAt)
                .build();
    }

}
