package com.malgn.domain.user.service.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.model.SearchUserEmployRequest;
import com.malgn.domain.user.model.UserEmploymentResponse;
import com.malgn.domain.user.model.v1.UserEmploymentV1Response;
import com.malgn.domain.user.repository.UserEmploymentRepository;
import com.malgn.domain.user.service.UserEmploymentService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserEmploymentV1Service implements UserEmploymentService {

    private final UserEmploymentRepository userEmploymentRepository;

    @Override
    public Page<UserEmploymentResponse> search(SearchUserEmployRequest searchRequest, Pageable pageable) {

        Page<UserEmployment> result = userEmploymentRepository.search(searchRequest, pageable);

        return result.map(UserEmploymentV1Response::from);
    }
}
