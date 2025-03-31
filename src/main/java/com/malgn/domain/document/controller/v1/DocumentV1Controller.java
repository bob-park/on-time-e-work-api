package com.malgn.domain.document.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.document.model.DocumentResponse;
import com.malgn.domain.document.model.v1.SearchDocumentV1Request;
import com.malgn.domain.document.service.v1.DocumentV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/documents")
public class DocumentV1Controller {

    private final DocumentV1Service documentService;

    @GetMapping(path = "")
    public Page<DocumentResponse> search(SearchDocumentV1Request searchRequest,
        @PageableDefault(size = 25, sort = "createdDate", direction = Direction.DESC) Pageable pageable) {
        return documentService.search(searchRequest, pageable);
    }
}
