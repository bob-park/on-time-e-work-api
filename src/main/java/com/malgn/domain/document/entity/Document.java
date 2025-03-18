package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseEntity;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "documents")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class Document extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private String userUniqueId;

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    private List<DocumentApprovalHistory> approvalHistories = new ArrayList<>();

    protected Document(Long id, DocumentType type, DocumentStatus status, String userUniqueId) {

        checkArgument(isNotEmpty(type), "type must be provided.");
        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");

        this.id = id;
        this.type = type;
        this.status = defaultIfNull(status, DocumentStatus.WAITING);
        this.userUniqueId = userUniqueId;
    }

    /*
     * 편의 메서드
     */
    public void addApprovalHistory(DocumentApprovalHistory approvalHistory) {
        approvalHistory.updateDocument(this);
        getApprovalHistories().add(approvalHistory);
    }

    public void approve() {
        this.status = DocumentStatus.APPROVED;
    }

    public void reject() {
        this.status = DocumentStatus.REJECTED;
    }
}
