package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.malgn.configure.proerties.AppProperties;
import com.malgn.domain.approval.repository.ApprovalLineRepository;
import com.malgn.domain.attendance.feign.AttendanceScheduleFeignClient;
import com.malgn.domain.document.processor.DelegatingApprovalProcessor;
import com.malgn.domain.document.processor.v1.VacationApprovalV1Processor;
import com.malgn.domain.document.provider.DelegatingCancelDocumentProvider;
import com.malgn.domain.document.provider.RequestDocumentProvider;
import com.malgn.domain.document.provider.v1.CancelVacationV1Provider;
import com.malgn.domain.document.provider.v1.RequestDocumentV1Provider;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.repository.DocumentRepository;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.google.provider.GoogleCalendarProvider;
import com.malgn.domain.notification.sender.DelegatingNotificationSender;
import com.malgn.domain.notification.sender.VacationDocumentNotificationSender;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;
import com.malgn.notification.client.NotificationClient;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {

    private final AppProperties properties;

    private final NotificationClient notificationClient;
    private final UserFeignClient userFeignClient;

    private final ApprovalLineRepository approvalLineRepository;
    private final DocumentRepository documentRepository;
    private final DocumentApprovalHistoryRepository historyRepository;
    private final VacationDocumentRepository vacationDocumentRepository;
    private final UserLeaveEntryRepository leaveEntryRepository;
    private final UserCompLeaveEntryRepository compLeaveEntryRepository;
    private final AttendanceScheduleFeignClient attendanceScheduleClient;

    @Bean
    public RequestDocumentProvider requestDocumentProvider() {
        return new RequestDocumentV1Provider(
            approvalLineRepository,
            documentRepository,
            historyRepository,
            delegatingNotificationSender());
    }

    @Bean
    public DelegatingApprovalProcessor delegatingApprovalProcessor() {
        DelegatingApprovalProcessor processor = new DelegatingApprovalProcessor();

        processor.add(
            new VacationApprovalV1Processor(
                attendanceScheduleClient,
                historyRepository,
                vacationDocumentRepository,
                leaveEntryRepository,
                notificationClient,
                userFeignClient,
                delegatingNotificationSender(),
                googleCalendarProvider()));

        return processor;
    }

    @Bean
    public DelegatingNotificationSender delegatingNotificationSender() {
        DelegatingNotificationSender sender = new DelegatingNotificationSender();

        sender.addSender(
            new VacationDocumentNotificationSender(
                notificationClient,
                userFeignClient,
                vacationDocumentRepository));

        return sender;
    }

    @Bean
    public GoogleCalendarProvider googleCalendarProvider() {
        return new GoogleCalendarProvider(properties.google());
    }

    @Bean
    public DelegatingCancelDocumentProvider delegatingCancelDocumentProvider() {
        DelegatingCancelDocumentProvider provider = new DelegatingCancelDocumentProvider();

        provider.add(new CancelVacationV1Provider(vacationDocumentRepository, leaveEntryRepository));

        return provider;
    }

}
