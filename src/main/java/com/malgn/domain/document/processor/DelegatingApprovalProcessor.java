package com.malgn.domain.document.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.type.DocumentType;

@Slf4j
public class DelegatingApprovalProcessor {

    private final List<ApprovalProcessor> processors = new ArrayList<>();

    public void add(ApprovalProcessor processor) {
        processors.add(processor);

        log.debug("added approval processor {}", processor.getClass().getSimpleName());
    }

    public DocumentApprovalHistory approval(Id<DocumentApprovalHistory, Long> id, DocumentType documentType) {

        for (ApprovalProcessor processor : processors) {
            if (processor.isSupport(documentType)) {
                return processor.approve(id);
            }
        }

        return null;
    }

    public DocumentApprovalHistory reject(Id<DocumentApprovalHistory, Long> id, String reason,
        DocumentType documentType) {

        for (ApprovalProcessor processor : processors) {
            if (processor.isSupport(documentType)) {
                return processor.reject(id, reason);
            }
        }

        return null;
    }

}
