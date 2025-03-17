package com.malgn.domain.approval.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
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

    private Long teamId;
    private String userUniqueId;

    private String contents;
    private String description;

    @Builder
    private ApprovalLine(Long id, Long teamId, String userUniqueId, String contents, String description) {

        checkArgument(isNotEmpty(teamId), "teamId must be provided.");
        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");
        checkArgument(StringUtils.isNotBlank(contents), "contents must be provided.");

        this.id = id;
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
}
