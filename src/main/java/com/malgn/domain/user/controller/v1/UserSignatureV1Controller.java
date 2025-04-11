package com.malgn.domain.user.controller.v1;

import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.user.model.UserSignatureResponse;
import com.malgn.domain.user.service.v1.UserSignatureV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users/{uniqueId}/signature")
public class UserSignatureV1Controller {

    private final UserSignatureV1Service userSignatureService;

    @GetMapping(path = "")
    public ResponseEntity<Resource> getSignature(@PathVariable String uniqueId) {
        Resource resource = userSignatureService.getSignature(uniqueId);

        return ResponseEntity.ok()
            .headers(headers -> {

                MediaType mediaType =
                    MediaTypeFactory.getMediaType(resource.getFilename())
                        .orElse(MediaType.APPLICATION_OCTET_STREAM);

                headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                        .filename(resource.getFilename(), StandardCharsets.UTF_8)
                        .build());

                headers.setContentType(mediaType);
            })
            .body(resource);
    }

    @PostMapping(path = "")
    public UserSignatureResponse updateSignature(@PathVariable String uniqueId,
        @RequestPart("signature") MultipartFile signatureFile) {
        return userSignatureService.updateSignature(uniqueId, signatureFile);
    }

    @PostMapping(path = "reset")
    public UserSignatureResponse resetSignature(@PathVariable String uniqueId) {
        return userSignatureService.resetSignature(uniqueId);
    }

}
