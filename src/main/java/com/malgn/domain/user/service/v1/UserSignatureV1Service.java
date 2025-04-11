package com.malgn.domain.user.service.v1;

import static com.malgn.domain.user.model.v1.UserSignatureV1Response.*;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.domain.user.entity.UserSignature;
import com.malgn.domain.user.model.UserSignatureResource;
import com.malgn.domain.user.model.UserSignatureResponse;
import com.malgn.domain.user.model.v1.UserSignatureV1Response;
import com.malgn.domain.user.repository.UserSignatureRepository;
import com.malgn.domain.user.service.UserSignatureService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserSignatureV1Service implements UserSignatureService {

    private final UserSignatureRepository userSignatureRepository;

    @Override
    public Resource getSignature(String userUniqueId) {

        UserSignature userSignature =
            userSignatureRepository.findByUserUniqueId(userUniqueId)
                .orElseThrow(() -> new NotFoundException(UserSignature.class, userUniqueId));

        if (userSignature.getSignatureImage() == null) {
            throw new NotFoundException(UserSignature.class, userUniqueId);
        }
        
        return new UserSignatureResource(
            userSignature.getSignatureImage(),
            userSignature.getUserUniqueId() + "." + userSignature.getFileExtension());
    }

    @Transactional
    @Override
    public UserSignatureResponse updateSignature(String userUniqueId, MultipartFile signatureFile) {

        UserSignature userSignature =
            userSignatureRepository.findByUserUniqueId(userUniqueId)
                .orElseGet(() -> {
                    UserSignature createdSignature =
                        UserSignature.builder()
                            .userUniqueId(userUniqueId)
                            .build();

                    createdSignature = userSignatureRepository.save(createdSignature);

                    log.debug("created user signature...");

                    return createdSignature;
                });

        try {
            userSignature.updateSignature(
                signatureFile.getBytes(),
                FilenameUtils.getExtension(signatureFile.getOriginalFilename()));

            log.debug("updated user signature...");
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        return from(userSignature);
    }

    @Transactional
    @Override
    public UserSignatureResponse resetSignature(String userUniqueId) {
        UserSignature userSignature =
            userSignatureRepository.findByUserUniqueId(userUniqueId)
                .orElseThrow(() -> new NotFoundException(UserSignature.class, userUniqueId));

        userSignature.resetSignature();

        log.debug("reset signature...");

        return from(userSignature);
    }
}
