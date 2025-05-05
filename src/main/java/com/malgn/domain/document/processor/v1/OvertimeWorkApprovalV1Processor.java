package com.malgn.domain.document.processor.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.entity.OvertimeWorkTime;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.processor.ApprovalProcessor;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.repository.OvertimeWorkDocumentRepository;
import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;

@Slf4j
@RequiredArgsConstructor
public class OvertimeWorkApprovalV1Processor implements ApprovalProcessor {

    private final DocumentApprovalHistoryRepository approvalHistoryRepository;
    private final OvertimeWorkDocumentRepository documentRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;
    private final UserCompLeaveEntryRepository userCompLeaveEntryRepository;

    private final UserFeignClient userClient;

    @Override
    public void approve(Id<DocumentApprovalHistory, Long> id) {
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        Id<OvertimeWorkDocument, Long> documentId = Id.of(OvertimeWorkDocument.class, document.getId());

        OvertimeWorkDocument overtimeWork =
            documentRepository.findDocument(documentId)
                .orElseThrow(() -> new NotFoundException(documentId));

        List<OvertimeWorkTime> registeredUserWorkTimes =
            overtimeWork.getTimes()
                .stream()
                .filter(item -> StringUtils.isNotBlank(item.getUserUniqueId()))
                .toList();

        for (OvertimeWorkTime workTime : registeredUserWorkTimes) {
            // 등록 사용자인 경우 보상휴가 처리
            if (!workTime.isDayOff()) {
                continue;
            }

            LocalDate workDate = workTime.getStartDate().toLocalDate();
            BigDecimal addCompLeaveDays = workTime.getCompensatoryDays();

            // user leave entry 증가 처리
            UserLeaveEntry userLeaveEntry =
                userLeaveEntryRepository.getLeaveEntry(
                        workTime.getUserUniqueId(),
                        workDate.getYear())
                    .orElseThrow(() -> new NotFoundException("No exist leave entry..."));

            userLeaveEntry.addTotalCompLeaveDays(addCompLeaveDays);

            // user comp leave entry 등록
            UserCompLeaveEntry createdCompLeaveEntry =
                UserCompLeaveEntry.builder()
                    .userUniqueId(workTime.getUserUniqueId())
                    .contents(workTime.getContents())
                    .effectiveDate(workDate)
                    .leaveDays(addCompLeaveDays)
                    .build();

            createdCompLeaveEntry = userCompLeaveEntryRepository.save(createdCompLeaveEntry);

            log.debug("created comp leave entry. ({})", createdCompLeaveEntry);

        }

        // 최종 승인자인 경우 최종 승인 처리
        document.approve();
    }

    @Override
    public void reject(Id<DocumentApprovalHistory, Long> id, String reason) {
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        history.reject(reason);
        document.reject();

        log.debug("rejected history. ({})", history);

    }

    @Override
    public boolean isSupport(DocumentType documentType) {
        return documentType == DocumentType.OVERTIME_WORK;
    }

}
