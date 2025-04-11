package com.malgn.domain.user.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.user.model.UserSignatureResponse;

public interface UserSignatureService {

    Resource getSignature(String userUniqueId);

    UserSignatureResponse updateSignature(String userUniqueId, MultipartFile signatureFile);

    UserSignatureResponse resetSignature(String userUniqueId);

}
