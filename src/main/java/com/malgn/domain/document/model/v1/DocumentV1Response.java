package com.malgn.domain.document.model.v1;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.ToString;

import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.model.DocumentResponse;

@ToString
public class DocumentV1Response implements DocumentResponse {

    private final Long id;
    private final DocumentType type;
    private final DocumentStatus status;
    private final String writerId;

    private final LocalDateTime createdDate;
    private final String createdBy;
    private final LocalDateTime lastModifiedDate;
    private final String lastModifiedBy;

    @Builder(toBuilder = true)
    protected DocumentV1Response(Long id, DocumentType type, DocumentStatus status, String writerId,
        LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.writerId = writerId;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;
    }

    public static DocumentResponse from(Document entity) {
        return DocumentV1Response.builder()
            .id(entity.getId())
            .type(entity.getType())
            .status(entity.getStatus())
            .writerId(entity.getWriterId())
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .build();
    }

    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public DocumentType type() {
        return this.type;
    }

    @Override
    public DocumentStatus status() {
        return this.status;
    }

    @Override
    public String writerId() {
        return this.writerId;
    }

    public LocalDateTime createdDate() {
        return this.createdDate;
    }

    public String createdBy() {
        return this.createdBy;
    }

    public LocalDateTime lastModifiedDate() {
        return this.lastModifiedDate;
    }

    public String lastModifiedBy() {
        return this.lastModifiedBy;
    }

}
