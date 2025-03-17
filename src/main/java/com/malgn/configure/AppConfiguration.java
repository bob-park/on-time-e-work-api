package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.malgn.domain.approval.repository.ApprovalLineRepository;
import com.malgn.domain.document.provider.RequestDocumentProvider;
import com.malgn.domain.document.provider.v1.RequestDocumentV1Provider;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.repository.DocumentRepository;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class AppConfiguration {

    private final ApprovalLineRepository approvalLineRepository;
    private final DocumentRepository documentRepository;
    private final DocumentApprovalHistoryRepository historyRepository;

    @Bean
    public RequestDocumentProvider requestDocumentProvider() {
        return new RequestDocumentV1Provider(approvalLineRepository, documentRepository, historyRepository);
    }

}
