package com.malgn.domain.document.service.v1;

import static com.malgn.domain.document.model.v1.DocumentV1Response.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.model.DocumentResponse;
import com.malgn.domain.document.model.SearchDocumentRequest;
import com.malgn.domain.document.model.v1.DocumentV1Response;
import com.malgn.domain.document.provider.DelegatingCancelDocumentProvider;
import com.malgn.domain.document.provider.RequestDocumentProvider;
import com.malgn.domain.document.provider.v1.DocumentV1Request;
import com.malgn.domain.document.repository.DocumentRepository;
import com.malgn.domain.document.service.DocumentService;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.model.UserResponse;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DocumentV1Service implements DocumentService {

    private final DocumentRepository documentRepository;

    private final RequestDocumentProvider requestProvider;
    private final DelegatingCancelDocumentProvider cancelProvider;

    private final UserFeignClient userClient;

    @Override
    public Page<DocumentResponse> search(SearchDocumentRequest searchRequest, Pageable pageable) {

        Page<Document> result = documentRepository.search(searchRequest, pageable);

        return result.map(DocumentV1Response::from);
    }

    @Transactional
    @Override
    public DocumentResponse request(Id<Document, Long> id) {

        Document document =
            documentRepository.findById(id.getValue())
                .orElseThrow(() -> new NotFoundException(id));

        UserResponse user = userClient.getById(document.getUserUniqueId());

        requestProvider.request(
            DocumentV1Request.builder()
                .documentId(id.getValue())
                .documentType(document.getType())
                .teamId(user.team().id())
                .build());

        return from(document);
    }

    @Transactional
    @Override
    public DocumentResponse cancel(Id<Document, Long> id) {

        Document document =
            documentRepository.findById(id.getValue())
                .orElseThrow(() -> new NotFoundException(id));

        cancelProvider.cancel(id, document.getType());

        log.debug("canceled document. (id={})", id.getValue());

        return DocumentV1Response.builder()
            .id(id.getValue())
            .build();
    }
}
