package com.malgn.domain.approval.service.v1;


import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.ObjectUtils;

import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.approval.model.v1.ApprovalLineV1Response;
import com.malgn.domain.approval.repository.ApprovalLineRepository;
import com.malgn.domain.approval.service.ApprovalLineService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ApprovalLineV1Service implements ApprovalLineService {

    private final ApprovalLineRepository approvalLineRepository;

    @Override
    public List<ApprovalLineResponse> getAll(Long teamId) {

        List<ApprovalLine> lines = approvalLineRepository.getLines(teamId);

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
