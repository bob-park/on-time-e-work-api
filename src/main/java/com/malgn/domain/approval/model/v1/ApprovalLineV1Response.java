package com.malgn.domain.approval.model.v1;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.approval.model.ApprovalLineResponse;

@Builder
public record ApprovalLineV1Response(Long id,
                                     ApprovalLineResponse parent,
                                     List<ApprovalLineResponse> children,
                                     Long teamId,
                                     String userUniqueId,
                                     String contents,
                                     String description,
                                     LocalDateTime createdDate,
                                     LocalDateTime lastModifiedDate)
    implements ApprovalLineResponse {

    public ApprovalLineV1Response {
        children = defaultIfNull(children, new ArrayList<>());
    }

    public static ApprovalLineResponse from(ApprovalLine line) {
        return ApprovalLineV1Response.builder()
            .id(line.getId())
            .teamId(line.getTeamId())
            .userUniqueId(line.getUserUniqueId())
            .contents(line.getContents())
            .description(line.getDescription())
            .createdDate(line.getCreatedDate())
            .lastModifiedDate(line.getLastModifiedDate())
            .build();
    }

    @Override
    public void addChild(ApprovalLineResponse child) {
        children.add(child);
    }

}
