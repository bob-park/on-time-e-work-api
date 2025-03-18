package com.malgn.domain.approval.service.v1;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.ObjectUtils;

import com.google.common.base.Preconditions;

import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.approval.model.SearchApprovalLineRequest;
import com.malgn.domain.approval.model.v1.ApprovalLineV1Response;
import com.malgn.domain.approval.model.v1.SearchApprovalLineV1Request;
import com.malgn.domain.approval.repository.ApprovalLineRepository;
import com.malgn.domain.approval.service.ApprovalLineService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ApprovalLineV1Service implements ApprovalLineService {

    private final ApprovalLineRepository approvalLineRepository;

    @Override
    public List<ApprovalLineResponse> getAll(SearchApprovalLineRequest searchRequest) {

        SearchApprovalLineV1Request searchV1Request = (SearchApprovalLineV1Request)searchRequest;

        checkArgument(isNotEmpty(searchV1Request.teamId()), "teamId must be provided.");

        List<ApprovalLine> lines = approvalLineRepository.getLines(searchRequest);

        List<ApprovalLineResponse> result =
            lines.stream()
                .filter(item -> isEmpty(item.getParent()))
                .map(ApprovalLineV1Response::from)
                .toList();

        for (ApprovalLineResponse item : result) {
            updateChildren(item, lines);
        }

        return result;
    }

    private void updateChildren(ApprovalLineResponse result, List<ApprovalLine> lines) {

        List<ApprovalLineResponse> children =
            lines.stream()
                .filter(item -> item.getParent() != null && result.id().equals(item.getParent().getId()))
                .map(ApprovalLineV1Response::from)
                .toList();

        for (ApprovalLineResponse child : children) {
            result.addChild(child);

            updateChildren(child, lines);
        }

    }

}
