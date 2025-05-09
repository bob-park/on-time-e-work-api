package com.malgn.domain.approval.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseTimeEntity;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.type.DocumentType;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "approval_lines")
public class ApprovalLine extends BaseTimeEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id")
    private ApprovalLine parent;

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<ApprovalLine> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private Long teamId;
    private String userUniqueId;

    private String contents;
    private String description;

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "approvalLine")
    private List<DocumentApprovalHistory> approvalHistories = new ArrayList<>();

    @Builder
    private ApprovalLine(Long id, DocumentType documentType, Long teamId, String userUniqueId, String contents,
        String description) {

        checkArgument(isNotEmpty(documentType), "documentType must be provided.");
        checkArgument(isNotEmpty(teamId), "teamId must be provided.");
        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");
        checkArgument(StringUtils.isNotBlank(contents), "contents must be provided.");

        this.id = id;
        this.documentType = documentType;
        this.teamId = teamId;
        this.userUniqueId = userUniqueId;
        this.contents = contents;
        this.description = description;
    }

    /*
     * 편의 메서드
     */
    public void updateParent(ApprovalLine parent) {
        this.parent = parent;
    }

    public void addChild(ApprovalLine child) {
        child.updateParent(this);
        getChildren().add(child);
    }

    public void addApprovalHistory(DocumentApprovalHistory approvalHistory) {
        approvalHistory.updateLine(this);

        getApprovalHistories().add(approvalHistory);
    }

    public ApprovalLine getNext() {
        if (getChildren().isEmpty()) {
            return null;
        }

        return getChildren().getFirst();
    }
}
