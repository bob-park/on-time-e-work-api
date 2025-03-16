package com.malgn.domain.document.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.document.model.VacationDocumentResponse;
import com.malgn.domain.document.model.v1.CreateVacationDocumentV1Request;
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

}
