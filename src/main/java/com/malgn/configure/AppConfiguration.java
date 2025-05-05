package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.malgn.configure.proerties.AppProperties;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.approval.repository.ApprovalLineRepository;
import com.malgn.domain.attendance.feign.AttendanceScheduleFeignClient;
import com.malgn.domain.document.processor.ApprovalProcessor;
import com.malgn.domain.document.processor.DelegatingApprovalProcessor;
import com.malgn.domain.document.processor.v1.OvertimeWorkApprovalV1Processor;
import com.malgn.domain.document.processor.v1.VacationApprovalV1Processor;
import com.malgn.domain.document.provider.DelegatingCancelDocumentProvider;
import com.malgn.domain.document.provider.RequestDocumentProvider;
import com.malgn.domain.document.provider.v1.CancelVacationV1Provider;
import com.malgn.domain.document.provider.v1.RequestDocumentV1Provider;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.repository.DocumentRepository;
import com.malgn.domain.document.repository.OvertimeWorkDocumentRepository;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.google.provider.GoogleCalendarProvider;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {

    private final AppProperties properties;

    private final OutboxEventPublisher publisher;

    private final UserFeignClient userFeignClient;
    private final AttendanceScheduleFeignClient attendanceScheduleClient;

    private final ApprovalLineRepository approvalLineRepository;
    private final DocumentRepository documentRepository;
    private final DocumentApprovalHistoryRepository historyRepository;
    private final VacationDocumentRepository vacationDocumentRepository;
    private final UserLeaveEntryRepository leaveEntryRepository;
    private final UserCompLeaveEntryRepository compLeaveEntryRepository;
    private final OvertimeWorkDocumentRepository overTimeWorkDocumentRepository;

    /*
     * request document provider
     */
    @Bean
    public RequestDocumentProvider requestDocumentProvider() {
        return new RequestDocumentV1Provider(
            approvalLineRepository,
            documentRepository,
            historyRepository,
            publisher);
    }

    /*
     * approval processors
     */
    @Bean
    public DelegatingApprovalProcessor delegatingApprovalProcessor() {
        DelegatingApprovalProcessor processor =
            new DelegatingApprovalProcessor(historyRepository, publisher);

        processor.add(vacationApprovalProcessor());
        processor.add(overtimeWorkApprovalProcessor());

        return processor;
    }

    @Bean
    public ApprovalProcessor vacationApprovalProcessor() {
        return new VacationApprovalV1Processor(
            attendanceScheduleClient,
            historyRepository,
            vacationDocumentRepository,
            leaveEntryRepository,
            userFeignClient,
            googleCalendarProvider());
    }

    @Bean
    public ApprovalProcessor overtimeWorkApprovalProcessor() {
        return new OvertimeWorkApprovalV1Processor(
            historyRepository,
            overTimeWorkDocumentRepository,
            leaveEntryRepository,
            compLeaveEntryRepository,
            userFeignClient);
    }

    /*
     * google calendar provider
     */
    @Bean
    public GoogleCalendarProvider googleCalendarProvider() {
        return new GoogleCalendarProvider(properties.google());
    }

    /*
     * cancel document provider
     */
    @Bean
    public DelegatingCancelDocumentProvider delegatingCancelDocumentProvider() {
        DelegatingCancelDocumentProvider provider = new DelegatingCancelDocumentProvider();

        provider.add(new CancelVacationV1Provider(vacationDocumentRepository, leaveEntryRepository));

        return provider;
    }

}
