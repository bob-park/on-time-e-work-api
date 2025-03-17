package com.malgn.domain.user.service.v1;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.model.SearchUserCompLeaveEntryRequest;
import com.malgn.domain.user.model.UserCompLeaveEntryResponse;
import com.malgn.domain.user.model.v1.UserCompLeaveEntryV1Response;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.service.UserCompLeaveEntryService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserCompLeaveEntryV1Service implements UserCompLeaveEntryService {

    private final UserCompLeaveEntryRepository userCompLeaveEntryRepository;

    @Override
    public List<UserCompLeaveEntryResponse> getAll(String userUniqueId, SearchUserCompLeaveEntryRequest searchRequest) {

        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");

        List<UserCompLeaveEntry> result = userCompLeaveEntryRepository.getAll(userUniqueId, searchRequest);

        return result.stream()
            .map(UserCompLeaveEntryV1Response::from)
            .toList();
    }
}
