package com.malgn.domain.document.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.model.VacationDocumentResponse;
import com.malgn.domain.document.model.v1.CreateVacationDocumentV1Request;
import com.malgn.domain.document.model.v1.SearchVacationDocumentV1Request;
import com.malgn.domain.document.service.v1.VacationDocumentV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/documents/vacations")
public class VacationDocumentV1Controller {

    private final VacationDocumentV1Service documentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public VacationDocumentResponse createVacation(@RequestBody CreateVacationDocumentV1Request createRequest) {
        return documentService.createDocument(createRequest);
    }

    @GetMapping(path = "")
    public Page<VacationDocumentResponse> search(SearchVacationDocumentV1Request searchRequest,
        @PageableDefault(size = 25, sort = "createdDate", direction = Direction.DESC) Pageable pageable) {
        return documentService.search(searchRequest, pageable);
    }

    @GetMapping(path = "{id:\\d+}")
    public VacationDocumentResponse getDocument(@PathVariable Long id) {
        return documentService.getById(Id.of(VacationDocument.class, id));
    }

}
