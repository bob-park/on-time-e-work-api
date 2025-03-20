package com.malgn.domain.user.service.v1;

import static com.google.common.base.Preconditions.*;
import static com.malgn.domain.user.model.v1.UserLeaveEntryV1Response.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.model.SearchUserLeaveEntryRequest;
import com.malgn.domain.user.model.UserLeaveEntryResponse;
import com.malgn.domain.user.model.v1.SearchUserLeaveEntryV1Request;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;
import com.malgn.domain.user.service.UserLeaveEntryService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserLeaveEntryV1Service implements UserLeaveEntryService {

    private final UserLeaveEntryRepository userLeaveEntryRepository;

    @Override
    public UserLeaveEntryResponse getLeaveEntry(String userUniqueId, SearchUserLeaveEntryRequest searchRequest) {

        SearchUserLeaveEntryV1Request searchV1Request = (SearchUserLeaveEntryV1Request)searchRequest;

        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");
        checkArgument(isNotEmpty(searchV1Request.year()), "year must be provided.");

        UserLeaveEntry result =
            userLeaveEntryRepository.getLeaveEntry(userUniqueId, searchV1Request.year())
                .orElseThrow(NotFoundException::new);

        return from(result);
    }
}
