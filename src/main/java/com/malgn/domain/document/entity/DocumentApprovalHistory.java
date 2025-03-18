package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import com.malgn.common.entity.BaseEntity;
import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.document.entity.type.ApprovalStatus;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "documents_approval_histories")
public class DocumentApprovalHistory extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private ApprovalLine approvalLine;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private String reason;

    @Builder
    private DocumentApprovalHistory(Long id, ApprovalStatus status, String reason) {

        this.id = id;
        this.status = defaultIfNull(status, ApprovalStatus.WAITING);
        this.reason = reason;
    }

    /*
     * 편의 메서드
     */
    public void updateDocument(Document document) {
        this.document = document;
    }

    public void updateLine(ApprovalLine approvalLine) {
        this.approvalLine = approvalLine;
    }

    public void approve() {
        checkArgument(getStatus() == ApprovalStatus.WAITING, "invalid status");

        this.status = ApprovalStatus.APPROVED;
    }

    public void reject(String reason) {

        checkArgument(getStatus() == ApprovalStatus.WAITING, "invalid status");

        this.status = ApprovalStatus.REJECTED;
        this.reason = reason;
    }

}
