package com.malgn.domain.document.service.v1;

import static com.malgn.domain.document.model.v1.VacationDocumentV1Response.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.model.CreateVacationDocumentRequest;
import com.malgn.domain.document.model.VacationDocumentResponse;
import com.malgn.domain.document.model.v1.CreateVacationDocumentV1Request;
import com.malgn.domain.document.model.v1.VacationDocumentV1Response;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.document.service.VacationDocumentService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VacationDocumentV1Service implements VacationDocumentService {

    private static final List<Integer> DEFAULT_FAMILY_DAYS_WEEKS = List.of(1, 3);
    private static final List<Integer> DEFAULT_WEEK_ENDS_ = List.of(3, 7);

    private final VacationDocumentRepository documentRepository;

    @Override
    public VacationDocumentResponse createDocument(CreateVacationDocumentRequest createRequest) {

        CreateVacationDocumentV1Request createV1Request = (CreateVacationDocumentV1Request)createRequest;

        VacationDocument createdDocument =
            VacationDocument.builder()
                .vacationType(createV1Request.vacationType())
                .vacationSubType(createV1Request.vacationSubType())
                .startDate(createV1Request.startDate())
                .endDate(createV1Request.endDate())
                .usedDays(
                    calculateUsedDate(
                        createV1Request.startDate(),
                        createV1Request.endDate(),
                        createV1Request.vacationSubType() != null))
                .reason(createV1Request.reason())
                .build();

        createdDocument = documentRepository.save(createdDocument);

        log.debug("created vacation document: {}", createdDocument);

        return from(createdDocument);
    }

    private BigDecimal calculateUsedDate(LocalDate startDate, LocalDate endDate, boolean isHalf) {

        BigDecimal result = BigDecimal.ZERO;

        int between = Period.between(startDate, endDate).getDays();

        return result;
    }

}
