package com.malgn.domain.document.provider.v1;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.event.DocumentCanceledEventPayload;
import com.malgn.domain.document.event.DocumentEventType;
import com.malgn.domain.document.provider.CancelDocumentProvider;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.entity.UserVacationUsedCompLeave;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;

@Slf4j
@RequiredArgsConstructor
public class CancelVacationV1Provider implements CancelDocumentProvider {

    private final VacationDocumentRepository documentRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;

    private final OutboxEventPublisher publisher;

    @Override
    public void cancel(Id<Document, Long> documentId) {

        VacationDocument document =
            documentRepository.getDocumentById(documentId)
                .orElseThrow(() -> new NotFoundException(documentId));

        // document status 가 approval 이 아닌 "WAITING" 인 경우 그냥 취소 처리
        if (document.getStatus() == DocumentStatus.PROCEEDING || document.getStatus() == DocumentStatus.WAITING) {
            document.cancel();
            return;
        }

        UserLeaveEntry userLeaveEntry =
            userLeaveEntryRepository.getLeaveEntry(document.getUserUniqueId(),
                    document.getStartDate().getYear())
                .orElseThrow(() -> new NotFoundException("No exist leave entry..."));

        // "APPROVED" 상태인 경우 처리
        switch (document.getVacationType()) {

            case GENERAL -> {
                // 연차 개수 원복
                userLeaveEntry.restoreLeaveDays(document.getUsedDays());
            }
            case COMPENSATORY -> {
                // 보상 휴가 개수 원복
                userLeaveEntry.restoreCompLeaveDays(document.getUsedDays());

                // 사용 보상 휴가 원복 처리
                List<UserVacationUsedCompLeave> usedCompLeaves = document.getUsedCompLeaves();

                for (UserVacationUsedCompLeave usedCompLeave : usedCompLeaves) {
                    usedCompLeave.getCompLeaveEntry().restoreUsedDays(usedCompLeave.getUsedDays());

                    usedCompLeave.updateUsedDays(BigDecimal.ZERO);
                }

            }
            case OFFICIAL -> {
                // 아무 차감하지 않는다.
            }

            default -> {
                // ignore
            }

        }

        document.cancel();

        log.debug("canceled vacation document. (id={})", document.getId());

        publisher.publish(
            DocumentEventType.DOCUMENT_CANCELED,
            DocumentCanceledEventPayload.builder()
                .id(document.getId())
                .type(document.getType())
                .userUniqueId(document.getUserUniqueId())
                .build());

    }

    @Override
    public boolean isSupport(DocumentType type) {
        return type == DocumentType.VACATION;
    }
}
