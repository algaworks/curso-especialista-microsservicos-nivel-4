package com.algaworks.algashop.product.catalog.presentation;

import com.algaworks.algashop.product.catalog.application.upload.UploadRequestApplicationService;
import com.algaworks.algashop.product.catalog.application.upload.UploadRequestInput;
import com.algaworks.algashop.product.catalog.application.upload.UploadResponseOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/upload-requests")
@RequiredArgsConstructor
public class UploadRequestController {

    private final UploadRequestApplicationService uploadRequestApplicationService;

    @PostMapping
    public UploadResponseOutput requestUpload(@RequestBody @Valid UploadRequestInput input) {
        return uploadRequestApplicationService.requestPreSignedUrl(input);
    }

}
