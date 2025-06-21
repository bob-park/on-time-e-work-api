package com.malgn.domain.user.service.v1;

import static com.malgn.domain.user.model.v1.UserEmploymentV1Response.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.exception.NotFoundException;
import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.model.SearchUserEmploymentRequest;
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
    public Page<UserEmploymentResponse> search(SearchUserEmploymentRequest searchRequest, Pageable pageable) {

        Page<UserEmployment> result = userEmploymentRepository.search(searchRequest, pageable);

        return result.map(UserEmploymentV1Response::from);
    }

    @Override
    public UserEmploymentResponse getEmploymentByUserId(String userUniqueId) {

        UserEmployment userEmployment =
            userEmploymentRepository.findByUserUniqueId(userUniqueId)
                .orElseThrow(() -> new NotFoundException(UserEmployment.class, userUniqueId));

        return from(userEmployment);
    }
}
