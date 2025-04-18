package com.malgn.domain.document.provider;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.type.DocumentType;

@Slf4j
public class DelegatingCancelDocumentProvider {

    private final List<CancelDocumentProvider> providers = new ArrayList<>();

    public void cancel(Id<Document, Long> documentId, DocumentType type) {

        for (CancelDocumentProvider provider : providers) {
            if (provider.isSupport(type)) {
                provider.cancel(documentId);
                return;
            }
        }

    }

    public void add(CancelDocumentProvider provider) {
        providers.add(provider);

        log.debug("added cancel document provider {}", provider.getClass().getSimpleName());
    }

}
