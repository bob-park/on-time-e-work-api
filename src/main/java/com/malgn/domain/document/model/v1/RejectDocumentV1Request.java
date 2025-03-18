package com.malgn.domain.document.model.v1;

import com.malgn.domain.document.model.RejectDocumentRequest;

public record RejectDocumentV1Request(String reason)
    implements RejectDocumentRequest {
}
