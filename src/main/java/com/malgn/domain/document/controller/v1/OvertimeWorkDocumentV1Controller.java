package com.malgn.domain.document.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.document.model.OvertimeWorkDocumentResponse;
import com.malgn.domain.document.model.v1.CreateOvertimeWorkDocumentV1Request;
import com.malgn.domain.document.service.v1.OvertimeWorkDocumentV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/documents/overtimes")
public class OvertimeWorkDocumentV1Controller {

    private final OvertimeWorkDocumentV1Service documentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public OvertimeWorkDocumentResponse createDocument(@RequestBody CreateOvertimeWorkDocumentV1Request createRequest) {
        return documentService.create(createRequest);
    }

}
